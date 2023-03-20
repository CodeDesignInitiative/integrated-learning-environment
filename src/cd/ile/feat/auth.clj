(ns cd.ile.feat.auth
  (:require [com.biffweb :as biff]
            [com.biffweb.impl.util :as butil]
            [com.biffweb.impl.time :as btime]
            [cd.ile.ui :as ui]
            [cd.ile.util :as util]
            [clj-http.client :as http]
            [com.biffweb.impl.xtdb :as bxt]
            [rum.core :as rum]
            [xtdb.api :as xt])
  (:import (java.security SecureRandom)))

;; For extra protection, you can call out to an email verification API here to
;; block spammy/high risk addresses.
(defn email-valid? [req email]
  (boolean (some->> email (re-matches #".+@.+\..+"))))

(defn signin-template [{:keys [to url]}]
  {:to             to
   :subject        "Sign in to My Application"
   :html-body      (rum/render-static-markup
                     [:html
                      [:body
                       [:p "Wir haben eine Anfrage zum Beitreten von ILE für diese Email Adresse erhalten."]
                       [:p [:a {:href url :target "_blank"} "Hier klicken zum Einloggen"]]
                       [:p "Falls du diese Email nicht seblst angefragt hast, kannst du sie ignorieren."]]])
   :message-stream "outbound"})

(defn send-link! [req email url]
  (and (email-valid? req email)
       (util/send-email
         req
         email
         url)))



(defn new-code [length]
  (let [rng (SecureRandom/getInstanceStrong)]
    (format (str "%0" length "d")
            (.nextInt rng (dec (int (Math/pow 10 length)))))))

(defn send-code! [{:keys [#_biff.auth/email-validator
                          biff/db
                          biff/send-email
                          biff.auth/get-user-id
                          params]
                   :as   ctx}]
  (let [email (:email params)
        code (new-code 6)
        user-id (delay (get-user-id db email))]
    (cond
      #_#_(not (email-validator ctx email))
      {:success false :error "invalid-email"}

      (not (send-email ctx
                       {:template    :signin-code
                        :to          email
                        :code        code
                        :user-exists (some? @user-id)}))
      {:success false :error "send-failed"}

      :else
      {:success true :email email :code code :user-id @user-id})))

(defn send-code-handler [{:keys [biff.auth/single-opt-in
                                 biff.auth/new-user-tx
                                 biff/db
                                 params]
                          :as ctx}]
  (let [{:keys [success error email code user-id]} (send-code! ctx)]
    (when success
      (bxt/submit-tx (assoc ctx :biff.xtdb/retry false)
                     (concat [{:db/doc-type :biff.auth/code
                               :db.op/upsert {:biff.auth.code/email email}
                               :biff.auth.code/code code
                               :biff.auth.code/created-at :db/now
                               :biff.auth.code/failed-attempts 0}]
                             (when (and single-opt-in (not user-id))
                               (new-user-tx ctx email)))))
    {:status 303
     :headers {"location" (if success
                            (str "/verify-code?email=" (:email params))
                            (str (:on-error params "/") "?error=" error))}}))

(defn verify-code-handler [{:keys [biff.auth/app-path
                                   biff.auth/new-user-tx
                                   biff.auth/get-user-id
                                   biff.xtdb/node
                                   biff/db
                                   params
                                   session]
                            :as req}]
  (let [email (:email params)
        code (bxt/lookup db :biff.auth.code/email email)
        success (and (some? code)
                     (< (:biff.auth.code/failed-attempts code) 3)
                     (not (btime/elapsed? (:biff.auth.code/created-at code) :now 3 :minutes))
                     (= (:code params) (:biff.auth.code/code code)))
        existing-user-id (when success (get-user-id db email))
        tx (cond
             success
             (concat [[::xt/delete (:xt/id code)]]
                     (when-not existing-user-id
                       (new-user-tx req email)))

             (and (not success)
                  (some? code)
                  (< (:biff.auth.code/failed-attempts code) 3))
             [{:db/doc-type :biff.auth/code
               :db/op :update
               :xt/id (:xt/id code)
               :biff.auth.code/failed-attempts [:db/add 1]}])]
    (bxt/submit-tx req tx)
    (if success
      {:status 303
       :headers {"location" app-path}
       :session (assoc session :uid (or existing-user-id
                                        (get-user-id db email)))}
      {:status 303
       :headers {"location" (str "/verify-code?error=invalid-code&email=" email)}})))

(defn send-token [{:keys [biff/base-url
                          biff/secret
                          anti-forgery-token
                          params]
                   :as   req}]
  (let [email (biff/normalize-email (:email params))
        code (new-code 6)
        token (biff/jwt-encrypt
                {:intent "signin"
                 :email  email
                 :state  (biff/sha256 anti-forgery-token)
                 :exp-in (* 60 60)}
                (secret :biff/jwt-secret))
        url (str base-url "/auth/verify/" token)]
    (if-not (util/email-signin-enabled? req)
      (do
        (println (str "Click here to sign in as " email ": " url))
        {:headers {"location" "/auth/printed/"}
         :status  303})
      {:status  303
       :headers {"location" (if (send-link! req email url)
                              "/auth/sent/"
                              "/auth/fail/")}})))

(defn verify-token [{:keys [biff.xtdb/node
                            biff/secret
                            path-params
                            session
                            anti-forgery-token] :as req}]
  (let [{:keys [intent email state]} (biff/jwt-decrypt (:token path-params)
                                                       (secret :biff/jwt-secret))
        success (and (= intent "signin")
                     (= state (biff/sha256 anti-forgery-token)))
        get-user-id #(biff/lookup-id (xt/db node) :user/email email)
        existing-user-id (when success (get-user-id))]
    (when (and success (not existing-user-id))
      (biff/submit-tx req
                      [{:db/doc-type    :user
                        :db.op/upsert   {:user/email email}
                        :user/joined-at :db/now}]))
    (if-not success
      {:status  303
       :headers {"location" "/auth/fail/"}}
      {:status  303
       :headers {"location" "/app"}
       :session (assoc session :uid (or existing-user-id (get-user-id)))})))

(defn signout [{:keys [session]}]
  {:status  303
   :headers {"location" "/"}
   :session (dissoc session :uid)})

(def signin-printed
  (ui/page
    {}
    [:div
     "The sign-in link was printed to the console. If you add API "
     "keys for Postmark and reCAPTCHA, the link will be emailed to you instead."]))

(def signin-sent
  (ui/page
    {}
    [:div.text-white "Wir haben dir einen Bestätigungslink an deine Email geschickt. Prüfe bitte dein Postfach."]))

(def signin-fail
  (ui/page
    {}
    [:div
     "Der Anmeldeversuch hat leider nicht geklappt. Es könnte daran liegen:"]
    [:ul
     [:li "We think your email address is invalid or high risk."]
     [:li "We tried to email the link to you, but there was an unexpected error."]
     [:li "You opened the sign-in link on a different device or browser than the one you requested it on."]]))

(def features
  {:routes [["/auth/send" {:post send-token}]
            ["/auth/verify/:token" {:get verify-token}]
            ["/auth/signout" {:post signout}]]
   :static {"/auth/printed/" signin-printed
            "/auth/sent/"    signin-sent
            "/auth/fail/"    signin-fail}})
