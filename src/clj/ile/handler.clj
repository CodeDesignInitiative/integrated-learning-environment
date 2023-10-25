(ns ile.handler
  (:require
    [ile.middleware :as middleware]
    [ile.layout :refer [error-page]]
    [ile.routes.home :as routes]
    [reitit.ring :as ring]
    [ile.env :refer [defaults]]
    [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop ((or (:stop defaults) (fn []))))

(defn- async-aware-default-handler
  ([_] nil)
  ([_ respond _] (respond nil)))


(mount/defstate app-routes
  :start
  (ring/ring-handler
    (ring/router
      [routes/public-routes
       routes/htmx-routes
       routes/private-routes
       routes/api-routes])
    (ring/create-default-handler
      {:not-found
       (constantly (error-page {:status 404, :title "Oh nein. Diese Seite existiert nicht. 😱"}))
       :method-not-allowed
       (constantly (error-page {:status 405, :title "Huch. Das darfst du nicht. 🚫"}))
       :not-acceptable
       (constantly (error-page {:status 406, :title "Ne. Kann ich nicht machen. 👾"}))})
    #_(ring/routes
        (ring/create-resource-handler
          {:path "/"})
        (ring/create-default-handler
          {:not-found
           (constantly (error-page {:status 404, :title "404 - Page not found"}))
           :method-not-allowed
           (constantly (error-page {:status 405, :title "405 - Not allowed"}))
           :not-acceptable
           (constantly (error-page {:status 406, :title "406 - Not acceptable"}))}))))

(defn app []
  (middleware/wrap-base #'app-routes))
