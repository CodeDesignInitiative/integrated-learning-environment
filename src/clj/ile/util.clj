(ns ile.util
  (:require
    [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))

(defn get-path-param-as-uuid [request param]
  (-> (get-in request [:path-params param]) parse-uuid))

(defn get-path-param [request param]
  (get-in request [:path-params param]))

(defn hidden-anti-forgery-field []
  [:input {:id    "__anti-forgery-token"
           :name  "__anti-forgery-token"
           :type  :hidden
           :value *anti-forgery-token*}])