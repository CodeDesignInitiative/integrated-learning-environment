(ns ile.story.core
  (:require
    [ile.story.handler :as handler]
    [ile.story.persistence :as persistence]))

(defn create-mission [mission]
  (persistence/create-mission mission))

(defn update-mission [mission]
  (persistence/update-mission mission))

(defn find-all-missions []
  (persistence/find-all-missions))

(defn find-mission [mission-id]
  (persistence/find-mission mission-id))

(def routes handler/routes)

(def api-routes handler/api-routes)