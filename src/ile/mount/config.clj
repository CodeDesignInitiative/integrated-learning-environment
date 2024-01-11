(ns ile.mount.config
  (:require
    [cprop.core :refer [load-config]]
    [cprop.source :as source]
    [mount.core :refer [args defstate]]))

#_:clj-kondo/ignore
(defstate env
          :start
          (load-config
            :merge
            [(args)
             (source/from-system-props)
             (source/from-env)]))
