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