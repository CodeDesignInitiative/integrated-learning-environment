(ns cd.ile
  (:require [com.biffweb :as biff]
            [cd.ile.components.app :as app]
            [cd.ile.components.auth :as auth]
            [cd.ile.components.home :as home]
            [cd.ile.components.worker :as worker]
            [cd.ile.schema :refer [malli-opts]]
            [clojure.test :as test]
            [clojure.tools.logging :as log]
            [nrepl.cmdline :as nrepl-cmd]
            [ring.middleware.refresh :as
             refresh]))

(def features
  [app/features
   auth/features
   home/features
   worker/features])

(def routes [["" {:middleware [biff/wrap-site-defaults]}
              (keep :routes features)]
             ["" {:middleware [biff/wrap-api-defaults]}
              (keep :api-routes features)]])

(def handler (-> (biff/reitit-handler {:routes routes})
                 biff/wrap-base-defaults))

(def static-pages (apply biff/safe-merge (map :static features)))

(defn generate-assets! [sys]
  (biff/export-rum static-pages "target/resources/public")
  (biff/delete-old-files {:dir  "target/resources/public"
                          :exts [".html"]}))

(defn on-save [sys]
  (biff/add-libs)
  (biff/eval-files! sys)
  (generate-assets! sys)
  (test/run-all-tests #"cd.ile.test.*"))

(def components
  [biff/use-config
   biff/use-secrets
   biff/use-xt
   biff/use-queues
   biff/use-tx-listener
   biff/use-wrap-ctx
   biff/use-jetty
   biff/use-chime
   (biff/use-when
     :cd.ile/enable-beholder
     biff/use-beholder)])

(defn start []
  (let [ctx (biff/start-system
              {:cd.ile/chat-clients   (atom #{})
               :biff/features         #'features
               :biff/after-refresh    `start
               :biff/handler          #'handler
               :biff/malli-opts       #'malli-opts
               :biff.beholder/on-save #'on-save
               :biff.xtdb/tx-fns      biff/tx-fns
               :biff/components       components})]
    (generate-assets! ctx)
    (log/info "Go to" (:biff/base-url ctx))))

(defn -main [& args]
  (start)
  (apply nrepl-cmd/-main args))
