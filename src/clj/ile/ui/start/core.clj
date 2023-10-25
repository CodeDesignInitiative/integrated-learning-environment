(ns ile.ui.start.core
  (:require
    [ile.dictonary.translations :as tr]
    [ile.ui.start.view :as view]))

(defn- start-screen [request]
  (let [lang (tr/lang request)
        is-admin? (boolean (some #{:admin} (get-in request [:session :user :user/roles])))
        is-teacher? (boolean (some #{:teacher} (get-in request [:session :user :user/roles])))]
    (println (get-in request [:session :user :user/roles]))
    (clojure.pprint/pprint (get-in request [:session]))
    (view/start-screen lang is-admin? is-teacher?)))

(def routes
  ["/:lang/" {:get start-screen}])
