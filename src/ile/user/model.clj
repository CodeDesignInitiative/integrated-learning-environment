(ns ile.user.model
  (:require
    [clojure.spec.alpha :as s]))

(s/def :xt/id uuid?)


(s/def :user/email string?)
(s/def :user/name string?)
(s/def :user/password string?)
(s/def :user/roles vector?)

(s/def :ile/user
  (s/keys :req [[:xt/id :user/name]
                :user/password
                :user/name]
          :opt [:user/roles]))
