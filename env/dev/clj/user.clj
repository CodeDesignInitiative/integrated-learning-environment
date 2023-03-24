(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
    [ile.core :refer [repl-server]]
    [ile.mount.xtdb :as mount-xtdb]
    [clojure.pprint]
    [clojure.spec.alpha :as s]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [clj-test-containers.core :as tc]
    [xtdb.api :as xtdb])
  (:import (org.testcontainers.containers PostgreSQLContainer)))

(alter-var-root #'s/explain-out (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn start-xtdb!
  "Starts our XTDB node"
  [container]
  (let [c ^PostgreSQLContainer (:container (tc/start! container))]
    (xtdb/start-node {:xtdb.jdbc/connection-pool {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
                                                  :db-spec {:host     (.getHost c)
                                                            :dbname   (.getDatabaseName c)
                                                            :user     (.getUsername c)
                                                            :password (.getPassword c)
                                                            :port     (.getMappedPort c (int 5432))}}

                      #_#_:xtdb/index-store {:kv-store {:xtdb/module 'xtdb.lmdb/->kv-store
                                                        :db-dir      (io/file "/tmp/lmdb")}}

                      :xtdb/tx-log               {:xtdb/module     'xtdb.jdbc/->tx-log
                                                  :connection-pool :xtdb.jdbc/connection-pool}

                      :xtdb/document-store       {:xtdb/module     'xtdb.jdbc/->document-store
                                                  :connection-pool :xtdb.jdbc/connection-pool}})))

(defn stop-xtdb!
  "Stops the XTDB node"
  [container]
  (.close mount-xtdb/node)
  (tc/stop! container))

(defn start
  "Starts application. You'll usually want to run this on startup.

  Creates and starts a Testcontainers instance as the backend for XTDB"
  []
  (let [container (-> {:container     (PostgreSQLContainer. "postgres:14.1")
                       :exposed-ports [5432]
                       :wait-for      {:wait-strategy   :port
                                       :startup-timeout 30}}
                      (tc/init)
                      (tc/bind-filesystem! {:host-path      "./.pg-data"
                                            :container-path "/var/lib/postgresql/data"
                                            :mode           :read-write}))]
    (-> (mount/find-all-states)
        (mount/except [#'repl-server])
        (mount/swap-states {#'ile.mount.xtdb/node {:start #(start-xtdb! container)
                                                            :stop  #(stop-xtdb! container)}})
        (mount/start))))

(defn stop
  "Stops application."
  []
  (mount/stop-except #'repl-server))

(defn restart
  "Restarts application."
  []
  (stop)
  (start))
