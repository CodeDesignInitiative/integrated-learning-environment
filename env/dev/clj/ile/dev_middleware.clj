(ns ile.dev-middleware
  (:require
    [ile.mount.config :refer [env]]
    [ile.user.core :as user]
    ;[ring.middleware.refresh :as refresh]
    [ring.middleware.reload :refer [wrap-reload]]
    [prone.middleware :refer [wrap-exceptions]]))

(comment
  (user/create-user (:test-user env)))

(defn wrap-test-user
  "Add a test user to the session to circumvent login for local dev."
  [handler]
  (fn [request]
    (if (:test-user env)
      (let [test-user (:test-user env)]
        (user/create-user test-user)
        (handler
          (-> request
              (assoc-in [:session :user] test-user)
              (assoc-in [:identity] true))))
      (handler request))))

(defn wrap-dev [handler]
  (-> handler
      ;refresh/wrap-refresh
      wrap-reload
      wrap-test-user
      ;; disable prone middleware, it can not handle async
      (cond-> (not (env :async?)) (wrap-exceptions {:app-namespaces ['ile]}))))
