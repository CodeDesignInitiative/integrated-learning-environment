(ns ile.user.core
  (:require
    [ile.user.persistence :as persistence]))


(defn find-user-by-id [user-id]
  (persistence/find-user-by-id user-id))

(defn find-user-by-email [user-email]
  (persistence/find-user-by-email user-email))

(defn create-user [user]
  (persistence/create-user user))