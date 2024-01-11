(ns ile.projects.model
  (:require
    [clojure.spec.alpha :as s]))


(s/def :user.project/name string?)
(s/def :user.project/code map?)
(s/def :user.project/owner string?)


(s/def :ile/user.project
  (s/keys :req [:xt/id
                :user.project/name
                :user.project/code
                :user.project/owner]))