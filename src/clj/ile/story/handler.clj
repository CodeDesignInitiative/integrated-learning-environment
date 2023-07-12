(ns ile.story.handler
  (:require
    [ile.dictonary.translations :as tr]
    [ile.story.view :as view]
    [ile.story.persistence :as persistence]
    [ile.ui.editor.core :as editor]
    [ile.util :as util]))

(defn mission-page [request]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        mission (persistence/find-mission mission-id)
        lang (tr/lang request)]
    (editor/block-editor lang mission)
    #_(view/mission-editor-page lang mission)))

(defn world-map-page [request]
  (let [world-id (keyword (util/get-path-param request :id))
        missions (persistence/find-all-missions)
        lang (tr/lang request)]
    (view/world-map-page lang {:name "HTML & CSS"
                               :subtitle "Einstieg"
                               :target "html-css"} missions)))

(defn worlds-page [request]
  (let [lang (tr/lang request)]
    (view/worlds-page lang)))

(def routes
  ["/:lang"
   ["/world"
    ["/map/:id" {:get world-map-page}]
    ["/mission/:id" {:get mission-page}]]
   ["/worlds" {:get worlds-page}]])