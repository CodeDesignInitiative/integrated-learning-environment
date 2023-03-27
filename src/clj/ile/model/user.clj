(ns ile.model.user
  (:require [clojure.spec.alpha :as s]
    ))

(s/def :xt/id uuid?)


(s/def :user/email string?)

(s/def :user/name string?)
(s/def :user/password string?)

(s/def :ile/user
  (s/keys :req [[:xt/id :user/email]
                :user/password
                :user/name]))

(s/def :user.project/name string?)
(s/def :user.project/html string?)
(s/def :user.project/css string?)
(s/def :user.project/owner string?)


(s/def :ile/user.project
  (s/keys :req [:xt/id
                :user.project/name
                :user.project/html
                :user.project/css
                :user.project/owner]))