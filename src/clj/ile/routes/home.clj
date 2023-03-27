(ns ile.routes.home
  (:require [buddy.auth :refer [authenticated?]]
            [clojure.pprint :refer [pprint]]
            [ile.components.app :as app]
            [ile.courses.html-website :as html-website]
            [ile.persistence :as persistence]
            [ile.views.chat-screen :as chat-screen]
            [ile.views.login-screen :as login-screen]
            [ring.util.response :as response]
            [ile.layout :as layout]
            [ile.middleware :as middleware]
            [ring.util.http-response :refer [content-type]]
            [ile.components.app :as app-components]
            [ile.views.projects :as projects-page]
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
    (println user)
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

(defn new-project [request]
  (println request)
  (println "........xxxxxxxxx.........")
  (let [template (get-in request [:form-params "template"])
        project-name (get-in request [:form-params "project-name"])
        user-email (-> (get-in request [:session :identity]) name)
        project-id (random-uuid)]
    (do
      (persistence/create-user-project (merge {:xt/id project-id}
                                              #:user.project{:name  project-name
                                                             :owner user-email
                                                             :css   (or (:css template) "")
                                                             :html  (or (:html template) "")}))
      (println "\n\nRedirect...\n\n")
      (response/redirect (str "/projekt?id=" project-id)))))

(defn project-editor [request]
  (let [project-id (get-in request [:query-params "id"])
        project (persistence/find-user-project (parse-uuid project-id))]
    (ile.views.editor-screen/editor-screen
      {:html {:code/line (:user.project/html project)}
       :css  {:code/base (:user.project/css project)}}
      nil nil)
    ))

(defn redirect-new-project [request]
  (let [template (get-in request [:form-params "template"])
        project-name (get-in request [:form-params "name"])
        user-email (-> (get-in request [:session :identity]) name)
        project-id (random-uuid)]

    (persistence/create-user-project (merge {:xt/id project-id}
                                            #:user.project{:name  project-name
                                                           :owner user-email
                                                           :css   (or (:css template) "")
                                                           :html  (or (:html template) "")}))
    (println "\n\nRedirect...\n\n")
    (response/redirect (str "/projekt?id=" project-id))
    ))

(defn save-project [request]
  (let [project-id (parse-uuid (get-in request [:form-params "id"]))
        html (get-in request [:form-params "html"])
        css (get-in request [:form-params "css"])
        project (persistence/find-user-project project-id)]
    (persistence/save-project (merge project
                                     {:user.project/html html
                                      :user.project/css  css}))
    (response/redirect (str "/projekt?id=" project-id)))
  )

(defn chat-screen [requset]
  (chat-screen/chat-screen html-website/start-chat-with-edna 1 "website1" 1)
  )

(def public-routes
  [""
   ["/login" {:post login
              :get  login-screen/login-page}]
   ["/register" {:post register
                 :get  login-screen/login-page}]])

(def private-routes
  ["" {:middleware [middleware/wrap-unauthorized-login-redirect
                    middleware/wrap-csrf
                    middleware/wrap-render-rum
                    middleware/wrap-formats]}
   ["/" {:get app/app}]
   ["/logout" {:get logout}]
   ["/chat" {:get chat-screen}]
   ["/wiki" {:get app-components/wiki}]
   ["/auftraege" {:get app-components/jobs}]
   ["/auftrag" {:get app-components/job-step}]
   ["/projekte"
    ["" {:get  projects-page/projects-page
         :post new-project}]
    ["/neu" {:post new-project
             :get  redirect-new-project}]
    ]
   ["/projekt"
    ["" {:get project-editor}]
    ["/speichern" {:post save-project}]]
   ["/editor" {:get app-components/editor}]
   ["/settings" {:get app-components/settings}]])