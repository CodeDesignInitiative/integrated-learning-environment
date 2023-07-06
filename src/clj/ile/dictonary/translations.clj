(ns ile.dictonary.translations
  (:require [taoensso.tempura :as tempura]))

(def dictionary
  {:de                                                   ; Locale
   {:missing ":en-GB missing text"                          ; Fallback for missing resources

    :login
    {:login "Anmelden"
     :register "Konto erstellen"
     :email "Email"
     :password "Passwort"
     :password-repeat "Passwort wiederholen"
     :username "Name"}}

   :ru                                                      ; A second locale
   {:missing      ":ru missing text"

    :login
    {:login "регистр"
     :register "зарегистрироваться"
     :email "электронная почта"
     :password "пароль"
     :password-repeat "повторить пароль"
     :username "Имя пользователя"}}})

(def opts {:dict dictionary})
(defn tr [lang kee]
  (tempura/tr {:dict dictionary} [lang] [kee])
  #_(partial tempura/tr opts lang))


(defn lang
  "Get language parameter from url as keyword"
  [request]
  (-> (get-in request [:path-params :lang]) keyword))

(defn url
  "Create url with language paramter as first directory in path.
  Concats url-args as string to url."
  [lang & url]
  (str "/" (name lang) (apply str url)))