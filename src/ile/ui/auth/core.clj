(ns ile.ui.auth.core
  (:require
    [ile.core.persistence :as p]
    [ile.mount.config :refer [env]]
    [ile.user.core :as user]
    [ile.core.util :as util]
    [ring.util.http-response :refer [content-type]]
    [crypto.password.bcrypt :as password]
    [ring.util.response :as response]
    [rum.core :as rum]

    [ile.dictonary.translations :as tr]
    [ile.layout :as layout]
    [ile.ui.auth.view :as view]))


(defn login [request]
  (let [user-name (get-in request [:form-params "user-name"])
        password (get-in request [:form-params "password"])
        user (user/find-user-by-id user-name)
        lang (tr/lang request)
        session (:session request)]
    (if user
      (if (password/check password (:user/password user))
        (let [next-url (get-in request [:query-params "next"] (str "/" (name lang) "/"))
              updated-session (-> session
                                  (assoc :identity (keyword user-name))
                                  (assoc :user user))]
          (println "\n\nPassword matched\n\n")
          (-> (response/redirect next-url)
              (assoc :session updated-session)))
        (do
          (println "\nWrong password\n\n")
          (response/redirect (str "/" (name lang) "/login?error=password"))))

      (do
        (println "\nUser does not exist\n\n")
        (response/redirect (str "/" (name lang) "/login?error=user"))))))

(defn login-email [request]
  (let [email (get-in request [:form-params "email"])
        password (get-in request [:form-params "password"])
        user (user/find-user-by-email email)
        lang (tr/lang request)
        session (:session request)]
    (if user
      (if (password/check password (:user/password user))
        (let [next-url (get-in request [:query-params "next"] (str "/" (name lang) "/"))
              updated-session (assoc session :identity (keyword email))]
          (println "\n\nPassword matched\n\n")
          (-> (response/redirect next-url)
              (assoc :session updated-session)))
        (do
          (println "\nWrong password\n\n")
          (response/redirect (str "/" lang "/login"))))

      (do
        (println "\nUser does not exist\n\n")
        (response/redirect "/login")))))

(defn register [request]
  (let [email (get-in request [:form-params "email"])
        password (get-in request [:form-params "password"])
        username (get-in request [:form-params "username"])
        user (user/find-user-by-id username)
        session (:session request)
        lang (tr/lang request)]
    (if user
      (layout/render-page (view/register-page lang :user-already-exists))
      (let [user (user/create-user {:xt/id         username
                                    :user/password (password/encrypt password)
                                    :user/email    email})]
        (-> (response/redirect "/")
            (assoc :session (assoc session :identity (keyword email))))))))

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
  (-> (response/redirect "/login")
      (assoc :session {})))

(defn- login-page [request]
  (let [lang (tr/lang request)
        error (get-in request [:params :error])]
    (layout/render-page
      (view/login-page lang error))))

(defn- register-page [request]
  (let [lang (tr/lang request)]
    (layout/render-page
      (view/register-page lang))))


(defn redirect-lang-de [_r]
  (response/redirect "/de/login"))

(defn create-admin-from-credentials [request]
  (let [admin-user {:xt/id         "root"
                    ; password is "rootroot", just encrypted
                    :user/password (password/encrypt (:admin-password env))
                    :user/email    "paul.hempel@code.design"
                    :user/roles    [:admin]}]
    (p/remove-from-db-and-wait "root")
    (user/create-user admin-user)
    (response/redirect "/login")))

(def routes
  [""
   ["/create-admin-from-credentials" {:get create-admin-from-credentials}]
   ["/logout" {:get logout}]
   ["/login" {:get redirect-lang-de}]
   ["/:lang/login" {:post login
                    :get  login-page}]
   ["/:lang/login-email" {:post login-email}]
   ["/:lang/register" {:post register
                       :get  register-page}]])



(comment
  (+ 1 1)
  )