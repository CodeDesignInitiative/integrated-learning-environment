(ns ile.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
       (log/info "\n-=[ile started successfully]=-"))
   :stop
   (fn []
       (log/info "\n-=[ile has shut down successfully]=-"))
   :middleware identity})
