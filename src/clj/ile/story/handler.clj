(ns ile.story.handler
  (:require
    [ring.util.http-response :as http-response]

    [ile.dictonary.translations :as tr]
    [ile.story.view :as view]
    [ile.story.persistence :as persistence]
    [ile.ui.editor.core :as editor]
    [ile.util :as util]))

(defn mission-page [request]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        mission (persistence/find-mission mission-id)
        lang (tr/lang request)]
    (editor/block-editor lang mission)))

(defn world-map-page [request]
  (let [world-id (keyword (util/get-path-param request :id))
        missions (persistence/find-all-missions)
        lang (tr/lang request)]
    (view/world-map-page lang {:name     "HTML & CSS"
                               :subtitle "Einstieg"
                               :target   "html-css"} missions)))

(defn worlds-page [request]
  (let [lang (tr/lang request)]
    (view/worlds-page lang)))

(defn finished-world-page [request]
  (let [lang (tr/lang request)]
    (view/finished-world-page lang)))

(def routes
  ["/:lang"
   ["/world"
    ["/finished" {:get finished-world-page}]
    ["/map/:id" {:get world-map-page}]
    ["/mission/:id" {:get mission-page}]]
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