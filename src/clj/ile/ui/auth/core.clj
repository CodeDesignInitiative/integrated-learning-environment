(ns ile.ui.auth.core
  (:require
    [ile.persistence :as persistence]
    [ring.util.http-response :refer [content-type]]
    [crypto.password.bcrypt :as password]
    [ring.util.response :as response]
    [rum.core :as rum]

    [ile.dictonary.translations :as tr]
    [ile.layout :as layout]
    [ile.ui.auth.view :as view]))


(defn login [request]
  (let [email (get-in request [:form-params "email"])
        password (get-in request [:form-params "password"])
        user (persistence/find-user email)
        session (:session request)]
    (if user
      (if (password/check password (:user/password user))
        (let [next-url (get-in request [:query-params "next"] "/")
              updated-session (assoc session :identity (keyword email))]
          (println "\n\nPassword matched\n\n")
          (-> (response/redirect next-url)
              (assoc :session updated-session)))
        (do
          (println "\nWrong password\n\n")
          (response/redirect "/login")))

      (do
        (println "\nUser does not exist\n\n")
        (response/redirect "/login")))))

(defn register [request]
  (let [email (get-in request [:form-params "email"])
        password (get-in request [:form-params "password"])
        username (get-in request [:form-params "username"])
        user (persistence/find-user email)
        session (:session request)]
    (println "user")
    (println user)
    (if user
      (do
        (println "\nUser already exists\n\n")
        (response/redirect "/login"))
      (let [user (persistence/create-user {:xt/id         username
                                           :user/password (password/encrypt password)
                                           :user/mail     email})]
        (if (not= user :user-already-exists)
          (-> (response/redirect "/")
              (assoc :session (assoc session :identity (keyword email))))
          (let [lang (tr/lang request)]
            (layout/render-page
              (view/register-page lang :user-already-exists))))))))

(defn logout
  [request]
  (content-type
    {:status  200
     :session nil
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body    (rum/render-static-markup
                [:html.full-height
                 [:head
                  [:link {:rel  :stylesheet
                          :href "/css/base.css?v=1"}]]
                 [:body.row.full-height
                  [:h1 "Logged Out"]
                  [:a {:href "/"} "Zum Start"]]])}

    "text/html; charset=utf-8")
  #_(-> (response/redirect "/login")
        (assoc :session {})))

(defn- login-page [request]
  (let [lang (tr/lang request)]
    (layout/render-page
      (view/login-page lang))))

(defn- register-page [request]
  (let [lang (tr/lang request)]
    (layout/render-page
      (view/register-page lang))))

(defn redirect-lang-de [r]
  (response/redirect "/de/login"))

(def routes
  [""
   ["/logout" {:get logout}]
   ["/login" {:get redirect-lang-de}]
   ["/:lang/login" {:post login
                    :get  login-page}]
   ["/:lang/register" {:post register
                       :get  register-page}]])