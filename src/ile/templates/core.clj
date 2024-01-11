(ns ile.templates.core
  (:require
    [ile.templates.persistence :as persistence]))

(defn find-all-templates []
  (persistence/find-all-templates))

(defn find-template [id]
  (persistence/find-template id))

(defn create-template! [template]
  (persistence/create-template! template))

(defn update-template! [template]
  (persistence/update-template! template))

