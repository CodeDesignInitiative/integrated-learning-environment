(ns ile.ui.auth.view
  (:require
    [ile.dictonary.translations :as tr]
    [ile.layout :as layout]))



(defn signin-form [lang]
  [:form.form-tile {:id     "signin-form"
                     :method :post
                     :action (str "/" (name lang) "/login")}
   [:label {:for "email"}
    (tr/tr lang :login/name-or-email)]
   [:.icon-input
    [:img {:src "/img/icons/mail.svg"}]
    [:input#email
     {:name        "email"
      :placeholder (tr/tr lang :login/user-name-placeholder)}]]
   #_[:label {:for "email"}
      (tr/tr lang :login/email)]
   #_[:input#email
      {:name         "email"
       :type         "email"
       :autocomplete "email"
       :placeholder  (tr/tr lang :login/email-placeholder)}]
   [:label {:for "password"}
    (tr/tr lang :login/password)]
   [:.icon-input
    [:img {:src "/img/icons/lock-closed.svg"}]
    [:input
     {:name        "password"
      :required    true
      :type        :password
      :placeholder "**********"
      :min-length  8}]]
   [:button {:type "submit"}
    (tr/tr lang :login/login) " →"]])

(defn signup-form [lang]
  [:form.form-tile {:id     "signup-form"
                     :method :post
                     :action (str "/" (name lang) "/register")}
   [:label {:for "username"}
    (tr/tr lang :login/username)]
   [:input#email
    {:name        "username"
     :required    true
     :placeholder (tr/tr lang :login/user-name-placeholder)}]
   [:label {:for "email"}
    (tr/tr lang :login/email)]
   [:input#email
    {:name         "email"
     :type         "email"
     :autocomplete "email"
     :placeholder  (tr/tr lang :login/email-placeholder)}]

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
    (tr/tr lang :login/register) " →"]])

(defn register-page [lang]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/")} "← " "Startseite"]
    [:a.button {:href (tr/url lang "/login") } "Zur Anmeldung →"]]
   [:main#register-page
    [:div
     [:h1 (tr/tr lang :login/register)]
     (signup-form lang)]
    [:aside
     [:h2 (tr/tr lang :login/important)]
     [:p (tr/tr lang :login/no-email-warning)]
     [:h2 (tr/tr lang :login/site-terms)]
     [:p (tr/tr lang :login/site-terms-description)]]]])

(defn login-page [lang]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/")} "← " "Zurück"]]
   [:main#login-page
    #_[:div
       [:div
        [:a {:href "/ru/login"} "RU"]]
       [:div
        [:a {:href "/de/login"} "DE"]]
       ]
    [:div
     [:h1 (tr/tr lang :login/login)]
     (signin-form lang)]
    [:aside
     [:h2 (tr/tr lang :login/no-account?)]
     [:a.button
      {:href (tr/url lang "/register")}
      [:img {:src "/img/icons/game-controller.svg"}]
      (tr/tr lang :login/learner-account)]
     [:a.button
      {:href (tr/url lang "/register")}
      [:img {:src "/img/icons/easel.svg"}]
      (tr/tr lang :login/teacher-account)]]]])