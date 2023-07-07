(ns ile.dictonary.translations
  (:require [taoensso.tempura :as tempura]))

(def dictionary
  {:de
   {:missing ":de-de missing text"

    :start
    {:story-tile   "Story"
     :editor-tile  "Editor"
     :wiki-tile    "Wiki"
     :language-btn "Sprache"
     :help-btn     "Hilfe ?!"}

    :login
    {:login                  "Anmelden"
     :register               "Konto erstellen"
     :no-account?            "Noch kein Konto?"
     :site-terms             "Nutzungsbedingungen"
     :site-terms-description "Durch die Erstellung des Kontos stimmst du den Nutzungsbedingungen zu. Weitere Informationen dazu findest du hier:"
     :important              "Wichtig!"
     :no-email-warning       "Wenn du keine Email Adresse angibst und dein Passwort vergisst, können wir dir nicht helfen dein Passwort zu ändern."
     :teacher-account        "Für Unterrichtende"
     :learner-account        "Für Lernende"
     :name-or-email          "Nutzername oder Email"
     :user-name              "Nutzername"
     :user-name-placeholder  "z.B. hacker_peep_3000 "
     :email                  "Email (freiwillig)"
     :email-placeholder      "z.B. name@example.com"
     :password               "Passwort"
     :password-repeat        "Passwort wiederholen"
     :username               "Name"}}

   :ru
   {:missing ":ru missing text"

    :start
    {:story-tile   "Story"
     :editor-tile  "Редактор"
     :wiki-tile    "Wiki"
     :language-btn "Языки"
     :help-btn     "Помощь ?!"}

    :login
    {:login                 "регистр"
     :register              "зарегистрироваться"
     ;:user-name "Nutzername"
     :user-name-placeholder "hacker_peep_3000 "
     :email                 "электронная почта"
     :email-placeholder     "name@example.com"
     :password              "пароль"
     :password-repeat       "повторить пароль"
     :username              "Имя пользователя"}}})

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