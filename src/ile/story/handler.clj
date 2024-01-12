(ns ile.story.handler
  (:require
    [ring.util.http-response :as http-response]

    [ile.dictonary.translations :as tr]
    [ile.story.view :as view]
    [ile.story.persistence :as persistence]
    [ile.core.persistence :as p]
    [ile.ui.editor.core :as editor]
    [ile.core.util :as util]))

(defn mission-page [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        learning-track-task-id (util/get-path-param-as-uuid request :m-id)
        mission (p/find-first-by-id learning-track-task-id)
        lang (tr/lang request)]
    (editor/block-editor lang mission learning-track-id)))

(defn world-map-page [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        learning-track (p/find-learning-track learning-track-id)
        learning-track-tasks (p/find-active-learning-track-tasks learning-track-id)
        lang (tr/lang request)]
    (view/learning-track-tasks-page lang learning-track learning-track-tasks)))

(defn worlds-page [request]
  (let [lang (tr/lang request)]
    (view/learning-tracks-page lang)))

(defn finished-world-page [request]
  (let [lang (tr/lang request)]
    (view/finished-world-page lang)))

(def routes
  ["/:lang"
   ["/world"
    ["/:id/finished" {:get finished-world-page}]
    ["/:id" {:get world-map-page}]
    ["/:id/mission/:m-id" {:get mission-page}]]
   ["/worlds" {:get worlds-page}]])


(defn get-mission-data [request]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        mission (persistence/find-mission mission-id)]
    (http-response/ok mission)))

(defn get-next-mission [request]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        mission (persistence/find-mission mission-id)
        next-mission (persistence/find-next-mission mission)]
    (if next-mission
      (http-response/ok {:mission-id (:xt/id next-mission)})
      (http-response/ok {:status "last_mission"}))))

(def api-routes
  [""
   ["/mission/:id" {:get get-mission-data}]
   ["/next-mission/:id" {:get get-next-mission}]])