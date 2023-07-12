(ns ile.util
  (:require [simple-email.core :as simple-email]
            [ile.mount.config :refer [env]]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))

(defn hostinger-smtp [recipent code]
  (->
    (simple-email/mail-server "smtp.hostinger.com" 587      ; host / port
                              true                          ; ssl
                              "app@code-editor.digital"     ; user
                              (:mail-pw env)                ; pw
                              "app@code-editor.digital")    ; from
    (simple-email/send-to recipent "LMS Code" (str code))))

(defn send-email [sys recipent code]
  (hostinger-smtp recipent code))

(defn get-path-param-as-uuid [request param]
  (-> (get-in request [:path-params param]) parse-uuid))


(defn get-path-param [request param]
  (get-in request [:path-params param]))

(defn hidden-anti-forgery-field []

  [:input {:id    "__anti-forgery-token"
           :name  "__anti-forgery-token"
           :type  :hidden
           :value *anti-forgery-token*}])