(ns cd.ile.feat.auth
  (:require [com.biffweb :as biff]
            [cd.ile.ui :as ui]
            [cd.ile.util :as util]
            [clj-http.client :as http]
            [rum.core :as rum]
            [xtdb.api :as xt]))

;; For extra protection, you can call out to an email verification API here to
;; block spammy/high risk addresses.
(defn email-valid? [req email]
  (boolean (some->> email (re-matches #".+@.+\..+"))))

(defn signin-template [{:keys [to url]}]
  {:to to
   :subject "Sign in to My Application"
   :html-body (rum/render-static-markup
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
        (signin-template {:to email :url url}))))

(defn send-token [{:keys [biff/base-url
                          biff/secret
                          anti-forgery-token
                          params]
                   :as req}]
  (let [email (biff/normalize-email (:email params))
        token (biff/jwt-encrypt
               {:intent "signin"
                :email email
                :state (biff/sha256 anti-forgery-token)
                :exp-in (* 60 60)}
               (secret :biff/jwt-secret))
        url (str base-url "/auth/verify/" token)]
    (if-not (util/email-signin-enabled? req)
      (do
        (println (str "Click here to sign in as " email ": " url))
        {:headers {"location" "/auth/printed/"}
         :status 303})
      {:status 303
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
        [{:db/doc-type :user
          :db.op/upsert {:user/email email}
          :user/joined-at :db/now}]))
    (if-not success
      {:status 303
       :headers {"location" "/auth/fail/"}}
      {:status 303
       :headers {"location" "/app"}
       :session (assoc session :uid (or existing-user-id (get-user-id)))})))

(defn signout [{:keys [session]}]
  {:status 303
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
   [:div "Wir haben dir einen Bestätigungslink an deine Email geschickt. Prüfe bitte dein Postfach."]))

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
  {:routes [["/auth/send"          {:post send-token}]
            ["/auth/verify/:token" {:get verify-token}]
            ["/auth/signout"       {:post signout}]]
   :static {"/auth/printed/" signin-printed
            "/auth/sent/" signin-sent
            "/auth/fail/" signin-fail}})
