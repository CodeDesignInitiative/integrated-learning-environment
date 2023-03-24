(ns ile.components.auth
  (:require [ile.ui :as ui]
            [ile.util :as util]
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


(defn signin [r]
  (clojure.pprint/pprint r))

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
  {:routes [["/auth/login" {:post signin}]
            ["/auth/signout" {:post signout}]]
   :static {"/auth/printed/" signin-printed
            "/auth/sent/"    signin-sent
            "/auth/fail/"    signin-fail}})
