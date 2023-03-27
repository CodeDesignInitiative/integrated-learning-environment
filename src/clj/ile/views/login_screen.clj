(ns ile.views.login-screen
  (:require [ile.layout :as layout]))


(defn signin-form []
  [:form.login-form {:id     "signin-form"
                     :method :post
                     :action "/login"}
   [:label {:for "email"}
    "Email"]
   [:input#email
    {:name         "email"
     :type         "email"
     :required     true
     :autocomplete "email"
     :placeholder  "Enter your email address"
     :class        '[border
                     border-gray-300
                     rounded
                     w-full
                     focus:border-teal-600
                     focus:ring-teal-600]}]
   [:label {:for "password"}
    "Passwort"]
   [:input
    {:name       "password"
     :required     true
     :type       :password
     :min-length 8}]
   [:button {:type "submit"}
    "Anmelden"]])

(defn signup-form []
  [:form.login-form {:id     "signup-form"
                     :method :post
                     :action "/register"}
   [:label {:for "email"}
    "Email"]
   [:input#email
    {:name         "email"
     :type         "email"
     :required     true
     :autocomplete "email"
     :placeholder  "Enter your email address"
     :class        '[border
                     border-gray-300
                     rounded
                     w-full
                     focus:border-teal-600
                     focus:ring-teal-600]}]
   [:label {:for "password"}
    "Passwort"]
   [:input
    {:name       "password"
     :required     true
     :type       :password
     :min-length 8}]
   [:label {:for "password2"}
    "Passwort wiederholen"]
   [:input
    {:name       "password2"
     :required     true
     :type       :password
     :min-length 8}]
   [:button {:type "submit"}
    "Registrieren"]])

(defn login-page [r]
  (layout/render-page
    [:div#login-page
     [:div
      [:h1 "Anmelden"]
      (signin-form)]
     [:div
      [:h1 "Registrieren"]
      (signup-form)]
     ]))