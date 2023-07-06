(ns ile.ui.start.core
  (:require
    [ile.ui.start.view :as view]
    [ile.util :as util]))

(defn- start-screen [request]
  (let [lang (util/lang request)]
    (view/start-screen lang)))

(def routes
  ["/:lang/" {:get start-screen}])
