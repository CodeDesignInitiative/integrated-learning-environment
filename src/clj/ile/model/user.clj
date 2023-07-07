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
