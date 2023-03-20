(ns cd.ile.util
  (:require [com.biffweb :as biff]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clj-http.client :as http]
            [simple-email.core :as simple-email]))

(defn email-signin-enabled? [{:keys [biff/secret]}]
  (secret :mail/login))

(defn hostinger-smtp [{:keys [biff/secret]} recipent code]
  (->
    (simple-email/mail-server "smtp.hostinger.com" 587      ; host / port
                              true                          ; ssl
                              "app@code-editor.digital"     ; user
                              (secret :mail/login)          ; pw
                              "app@code-editor.digital")    ; from
    (simple-email/send-to recipent "LMS Code" (str code))))

(defn send-email [sys recipent code]
  (hostinger-smtp sys recipent code))
