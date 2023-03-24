(ns ile.env
  (:require
    [clojure.tools.logging :as log]
    [ile.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[ile started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[ile has shut down successfully]=-"))
   :middleware wrap-dev})
