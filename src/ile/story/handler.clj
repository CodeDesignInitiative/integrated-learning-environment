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


(defn get-learning-track-task [request]
  (let [learning-track-task-id (util/get-path-param-as-uuid request :id)
        learning-track-task (p/find-first-by-id learning-track-task-id)
        learning-track (p/find-first-by-id (:learning-track-task/learning-track learning-track-task))]
    (http-response/ok (merge learning-track-task learning-track))))

(defn get-next-learning-track-task [request]
  (let [learning-track-task-id (util/get-path-param-as-uuid request :id)
        learning-track-task (p/find-first-by-id learning-track-task-id)
        next-learning-track-task (p/find-next-learning-track-task learning-track-task)]
    (clojure.pprint/pprint (:xt/id next-learning-track-task))
    (if next-learning-track-task
      (http-response/ok {:next-learning-track-task-id (:xt/id next-learning-track-task)})
      (http-response/ok {:status "last_mission"}))))

(def api-routes
  [""
   ["/learning-track-task/:id" {:get get-learning-track-task}]
   ["/next-learning-track-task/:id" {:get get-next-learning-track-task}]])