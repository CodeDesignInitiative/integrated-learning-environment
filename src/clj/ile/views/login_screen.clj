(ns ile.views.login-screen
  (:require
    [ile.dictonary.translations :as tr]
    [ile.layout :as layout]))


(defn signin-form [lang]
  [:form.login-form {:id     "signin-form"
                     :method :post
                     :action (str "/" (name lang) "/login")}
   [:label {:for "email"}
    (tr/tr lang :login/email)]
   [:input#email
    {:name         "email"
     :required     true
     :autocomplete "email"
     :placeholder  "username or name@example.com"}]
   [:label {:for "password"}
    (tr/tr lang :login/password)]
   [:input
    {:name       "password"
     :required   true
     :type       :password
     :placeholder "**********"
     :min-length 8}]
   [:button {:type "submit"}
    (tr/tr lang :login/login)]])

(defn signup-form [lang]
  [:form.login-form {:id     "signup-form"
                     :method :post
                     :action (str "/" (name lang) "/register")}
   [:label {:for "email"}
    (tr/tr lang :login/email)]
   [:input#email
    {:name         "email"
     :type         "email"
     :required     true
     :autocomplete "email"
     :placeholder  "name@example.com"}]
   [:label {:for "username"}
    (tr/tr lang :login/username)]
   [:input#email
    {:name        "username"
     :required    true
     :placeholder "Pablo"}]
   [:label {:for "password"}
    (tr/tr lang :login/password)]
   [:input
    {:name       "password"
     :required   true
     :type       :password
     :placeholder "**********"
     :min-length 8}]
   [:label {:for "password2"}
    (tr/tr lang :login/password-repeat)]
   [:input
    {:name       "password2"
     :required   true
     :type       :password
     :placeholder "**********"
     :min-length 8}]
   [:button {:type "submit"}
    (tr/tr lang :login/register)
    ]])

(defn login-page [request]
  (let [lang (tr/lang request)]
    (layout/render-page
      [:div#login-page
       [:div
        [:div
         [:a {:href "/ru/login"} "RU"]]
        [:div
         [:a {:href "/de/login"} "DE"]]
        ]
       [:div
        [:h1 (tr/tr lang :login/login)]
        (signin-form lang)]
       [:div
        [:h1 (tr/tr lang :login/register)]
        (signup-form lang)]])))