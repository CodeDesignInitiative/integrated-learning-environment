(ns ile.projects.core
  (:require
    [ile.projects.persistence :as persistence]
    [ile.projects.model]))

(defn create-user-project [project]
  (persistence/create-user-project project))

(defn find-user-project [id]
  (persistence/find-user-project id))

(defn find-user-projects [user-email]
  (persistence/find-user-projects user-email))

(defn save-project [project]
  (persistence/save-project project))