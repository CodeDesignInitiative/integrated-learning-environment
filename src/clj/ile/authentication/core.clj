(ns ile.authentication.core)


(defn is-admin? [request]
  (boolean (some #{:admin} (get-in request [:session :user :user/roles]))))