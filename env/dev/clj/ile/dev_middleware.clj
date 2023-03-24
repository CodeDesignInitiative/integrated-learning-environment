(ns ile.dev-middleware
  (:require
    [ile.mount.config :refer [env]]
    [ring.middleware.reload :refer [wrap-reload]]
    [prone.middleware :refer [wrap-exceptions]]))

(defn wrap-dev [handler]
  (-> handler
      #_refresh/wrap-refresh
      wrap-reload
      ;; disable prone middleware, it can not handle async
      (cond-> (not (env :async?)) (wrap-exceptions {:app-namespaces ['ile]}))))
