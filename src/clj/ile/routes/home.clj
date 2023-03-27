(ns ile.routes.home
  (:require [buddy.auth :refer [authenticated?]]
            [ile.components.app :as app]
            [ile.persistence :as persistence]
            [ile.views.login-screen :as login-screen]
            [ring.util.response :as response]
            [ile.layout :as layout]
            [ile.middleware :as middleware]
            [ring.util.http-response :refer [content-type]]
            [ile.components.app :as app-components]

            [crypto.password.bcrypt :as password]
            [rum.core :as rum]))





(defn home [request]
  (if-not (authenticated? request)
    (response/redirect "/login")
    (layout/render-page

      [:h1 "Hello " (get-in request [:session :identity])])))


(defn login [request]
  (let [email (get-in request [:form-params "email"])
        password (get-in request [:form-params "password"])
        user (persistence/find-user email)
        session (:session request)]
    (if user
      (if (password/check password (:user/password user))
        (let [next-url (get-in request [:query-params "next"] "/")
              updated-session (assoc session :identity (keyword email))]
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
    (println user)
    (if user
      (do
        (println "\nUser already exists\n\n")
        (response/redirect "/login"))
      (let [user (persistence/create-user {:xt/id         email
                                           :user/password (password/encrypt password)
                                           :user/name     username})
            updated-session (assoc session :identity (keyword email))]
        (-> (response/redirect "/")
            (assoc :session updated-session))))))

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
                  [:h1 "Logged Out"]]])}

    "text/html; charset=utf-8")
  #_(-> (response/redirect "/login")
        (assoc :session {})))

(def public-routes
  [""
   ["/login" {:post login
              :get  login-screen/login-page}]
   ["/register" {:post register
                 :get  login-screen/login-page}]])

(def private-routes
  ["" {:middleware [middleware/wrap-unauthorized-login-redirect
                    middleware/wrap-render-rum
                    middleware/wrap-csrf
                    middleware/wrap-formats]}
   ["/" {:get app/app}]
   ["/logout" {:get logout}]
   ["/chat" {:get app-components/chat}]
   ["/wiki" {:get app-components/wiki}]
   ["/auftraege" {:get app-components/jobs}]
   ["/auftrag" {:get app-components/job-step}]
   ["/editor" {:get app-components/editor}]
   ["/settings" {:get app-components/settings}]
   ])