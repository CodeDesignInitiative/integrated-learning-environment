(ns ile.routes.home
  (:require
    [ile.middleware :as middleware]
    [ile.story.core :as story-page]
    [ile.ui.admin.core :as admin-page]
    [ile.ui.legal.core :as legal-page]
    [ile.ui.projects.core :as projects-page]
    [ile.ui.start.core :as start-page]
    [ring.util.response :as response]))

(defn redirect-start-lang-de [r]
  (response/redirect "/de/"))

(def routes
  ["" {:middleware [middleware/wrap-csrf
                    middleware/wrap-render-rum
                    middleware/wrap-formats]}

   ["/" {:get redirect-start-lang-de}]
   start-page/routes
   projects-page/routes
   admin-page/routes
   story-page/routes
   ["/datenschutz" {:get legal-page/privacy-statement}]])
