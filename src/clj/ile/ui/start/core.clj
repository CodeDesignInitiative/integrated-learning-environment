(ns ile.ui.start.core
  (:require
    [ile.dictonary.translations :as tr]
    [ile.ui.start.view :as view]))

(defn- start-screen [request]
  (let [lang (tr/lang request)]
    (view/start-screen lang)))

(def routes
  ["/:lang/" {:get start-screen}])
