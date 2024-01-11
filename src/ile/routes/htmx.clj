(ns ile.routes.htmx
  (:require
    [ile.middleware :as middleware]
    [ile.ui.admin.core :as admin-page]))

(def routes
  ["/htmx" {:middleware [middleware/wrap-render-htmx
                         middleware/wrap-unauthorized-login-redirect
                         middleware/wrap-csrf
                         middleware/wrap-render-rum
                         middleware/wrap-formats]}
   admin-page/htmx-routes])