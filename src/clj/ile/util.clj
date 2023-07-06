(ns ile.util
  (:require [simple-email.core :as simple-email]
            [ile.mount.config :refer [env]]))

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


(defn lang [request]
  (-> (get-in request [:path-params :lang]) keyword))

(defn lang-url [lang & url]
  (str "/" (name lang) (apply str url)))