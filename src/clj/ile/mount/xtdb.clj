(ns ile.mount.xtdb
  "Mount configuration for XTDB"
  (:require [xtdb.api :as xtdb]
            [mount.core :refer [defstate]]
            [ile.mount.config :refer [env]]))

(declare start-xtdb!)
(declare stop-xtdb!)

#_:clj-kondo/ignore
(defstate node
          :start (start-xtdb!)
          :stop (stop-xtdb!))

(defn start-xtdb!
  "Starts our XTDB node"
  []
  (if-let [db-spec (:db-spec env)]
    (xtdb/start-node
      {:xtdb.jdbc/connection-pool {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
                                   :db-spec db-spec}

       #_#_:xtdb/index-store {:kv-store {:xtdb/module 'xtdb.lmdb/->kv-store
                                         :db-dir      (io/file "/tmp/lmdb")}}

       :xtdb/tx-log               {:xtdb/module     'xtdb.jdbc/->tx-log
                                   :connection-pool :xtdb.jdbc/connection-pool}

       :xtdb/document-store       {:xtdb/module     'xtdb.jdbc/->document-store
                                   :connection-pool :xtdb.jdbc/connection-pool}})
    (throw (new IllegalArgumentException
                "No XTDB configuration found, please provide the :db-spec configuration"))))

(defn stop-xtdb!
  "Stops the XTDB node"
  []
  (.close node))
