(ns ile.routes.home
  (:require [ile.components.home :as home]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [ile.components.app :as app]
            [ile.views.login-screen :as login-screen]
            [ring.util.response :as response]
            [ile.layout :as layout]
            [ile.middleware :as middleware]
            [ile.mount.xtdb :as xtdb]
            [ring.util.http-response :refer [content-type ok]]
            [ile.components.app :as app-components]
            [rum.core :as rum]
            [xtdb.api :as xt]))





(defn home [request]
  (if-not (authenticated? request)
    (response/redirect "/login")
    (layout/render-page

      [:h1 "Hello " (get-in request [:session :identity])])))

(defn logged-in? [params]
  false
  )


(def authdata
  "Global var that stores valid users with their
   respective passwords."
  {:admin "secret"
   :test  "secret"})

(defn- flatten-result
  "Takes the XTDB typical result (a set containing vectors) and combines the
  entries into a single vector"
  [result]
  (vec (mapcat (fn [e] e) result)))

(defn- query-args-count-match?
  "Checks whether the number of arguments required for the query match the provided
  number of arguments.

  Based on: https://github.com/jacobobryant/biff - Copyright (c) 2023 Jacob O'Bryan
  License:  https://github.com/jacobobryant/biff/blob/master/LICENSE"
  [query args]

  (when-not (= (count (first (:in query)))
               (count args))
    (throw (ex-info (str "Incorrect number of query arguments. Expected "
                         (count (first (:in query)))
                         " but got "
                         (count args)
                         ".")
                    {}))))

(defn query-db
  "Executes a xtdb query with the provided arguments and returns a flattened result vector."
  ([query & args]
   (query-args-count-match? query args)
   (-> (xt/db xtdb/node)
       (xt/q query (when args (vec args)))
       flatten-result))
  ([query]
   (-> (xt/db xtdb/node)
       (xt/q query)
       flatten-result)))

(defn find-first
  [query & args]
  (-> (apply query-db query args)
      first))

(defn find-user
  [email]
  (find-first '{:find  [(pull ?user [* :ile/user])]
                :where [[?user :xt/id email]]
                :in    [[email]]}
              email))

(defn put-in-db [& documents]
  (let [puts (vec (map (fn [v] [::xt/put v]) documents))]
    (xt/submit-tx xtdb/node puts)))

(defn put-in-db-and-wait [& documents]
  (xt/await-tx xtdb/node (apply put-in-db documents)))

(defn create-user
  [user]
  (put-in-db-and-wait user)
  user)

(defn login [request]
  (let [email (get-in request [:form-params "email"])
        password (get-in request [:form-params "password"])
        user (find-user email)
        session (:session request)
        found-password (get authdata (keyword email))]
    (if user
      (if (and found-password (= found-password password))
        (let [next-url (get-in request [:query-params "next"] "/")
              updated-session (assoc session :identity (keyword email))]
          (-> (response/redirect next-url)
              (assoc :session updated-session)))
        (login-screen/login-page request)
        )
      (let [user (create-user {:xt/id         email
                               :user/password password
                               :user/name     ""})
            updated-session (assoc session :identity (keyword email))]
        (-> (response/redirect "/")
            (assoc :session updated-session))
        ))))

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
   ])

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