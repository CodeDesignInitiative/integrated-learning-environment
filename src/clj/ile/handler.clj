(ns ile.handler
  (:require
    [ile.middleware :as middleware]
    [ile.layout :refer [error-page]]
    [ile.routes.home :as home]
    [ile.routes.api :as api]
    [ile.routes.htmx :as htx]
    [ile.routes.public :as public]
    [reitit.ring :as ring]
    [ile.env :refer [defaults]]
    [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop ((or (:stop defaults) (fn []))))

(mount/defstate app-routes
  :start
  (ring/ring-handler
    (ring/router
      [public/routes
       htx/routes
       home/routes
       api/routes])
    (ring/create-default-handler
      {:not-found
       (constantly (error-page {:status 404, :title "Oh nein. Diese Seite existiert nicht. 😱"}))
       :method-not-allowed
       (constantly (error-page {:status 405, :title "Huch. Das darfst du nicht. 🚫"}))
       :not-acceptable
       (constantly (error-page {:status 406, :title "Ne. Kann ich nicht machen. 👾"}))})))

(defn app []
  (middleware/wrap-base #'app-routes))
