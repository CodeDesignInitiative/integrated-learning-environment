(ns ile.model.user
  (:require [clojure.spec.alpha :as s]
    ))



(s/def :xt/id string?)

(s/def :user/name string?)
(s/def :user/password string?)

(s/def :ile/user
  (s/keys :req [:xt/id
                :user/password
                :user/name]))