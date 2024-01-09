(ns ile.routes.api
  (:require
    [ile.story.core :as story-page]
    [muuntaja.middleware :as muuntaja-middleware]))


(def routes
  ["/api" {:middleware [muuntaja-middleware/wrap-format-response]}
   story-page/api-routes])
