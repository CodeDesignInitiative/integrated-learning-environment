(ns ile.core.util
  (:require
    [clojure.walk :as walk]
    [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))

(defn get-path-param-as-uuid [request param]
  (-> (get-in request [:path-params param]) parse-uuid))

(defn has-path-param? [request param]
  (some? (get-in request [:path-params param])))

(defn get-path-param [request param]
  (get-in request [:path-params param]))

(defn get-form-params [request]
  (-> (:form-params request)
      (dissoc "__anti-forgery-token")
      (walk/keywordize-keys)))

(defn hidden-anti-forgery-field []
  [:input {:id    "__anti-forgery-token"
           :name  "__anti-forgery-token"
           :type  :hidden
           :value *anti-forgery-token*}])

