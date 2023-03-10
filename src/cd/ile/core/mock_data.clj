(ns cd.ile.core.mock-data
  (:import (java.time LocalDate)))


(def notes ["Chat öffnen"
            "Code Editor reinschauen"
            "Aufträge checken"])

(def notifications [{:date    (LocalDate/of 2023 2 13)
                     :heading "Willkommen im Team!"
                     :msg     "Damit du hier einen guten Start hast, kümmert sich Xara um dich. Sie sollte dir schon geschrieben haben. Schau mal in den Chat.\nIch habe es dir auch als Erinnerung auf deinen digitalen Notizzettel geschrieben."}
                    {:date    (LocalDate/of 2023 2 21)
                     :heading "Du hast einen neuen Auftrag!"
                     :msg     "Schau in deine Aufträge links und sieh nach was es so gibt."}
                    {:date    (LocalDate/of 2023 2 24)
                     :heading "Danke für deine Hilfe bei dem letzten Projekt."
                     :msg     "..."}
                    {:date    (LocalDate/of 2023 2 13)
                     :heading "Willkommen im Team!"
                     :msg     "Damit du hier einen guten Start hast, kümmert sich Xara um dich. Sie sollte dir schon geschrieben haben. Schau mal in den Chat.\nIch habe es dir auch als Erinnerung auf deinen digitalen Notizzettel geschrieben."}
                    {:date    (LocalDate/of 2023 2 21)
                     :heading "Du hast einen neuen Auftrag!"
                     :msg     "Schau in deine Aufträge links und sieh nach was es so gibt."}
                    {:date    (LocalDate/of 2023 2 24)
                     :heading "Danke für deine Hilfe bei dem letzten Projekt."
                     :msg     "..."}])

(def person-edna #:person{:id           (random-uuid)
                          :name         "Edna"
                          :organization "Ausbruchs GmbH"})

(def chat-with-edna [#:story{:person  person-edna
                             :message "Kennst du schon dieses Video?"
                             :video   "https://www.youtube-nocookie.com/embed/lXMskKTw3Bc"}
                     #:story{:person         person-edna
                             :message        "Das ist wahre Kunst, findest du nicht auch?"
                             :answer-choices ["So was von!"
                                              "Total knorke war das!"
                                              "Da gebe ich dir voll recht!"]}
                     #:story{:person         person-edna
                             :message        "Jetzt mal was anderes. Kannst du HTML?"
                             :answer-choices ["Nein"
                                              "Ein bisschen"
                                              "Aber hallo!"]}
                     #:story{:person  person-edna
                             :message "Alles klar, ist eigentlich egal, wir brauchen Hilfe und du musst nur Zeit mitbringen."}])

(def conversations [#:conversation{:with           person-edna
                                   :open-chats     [chat-with-edna]
                                   :previous-chats [chat-with-edna chat-with-edna chat-with-edna]}
                    #:conversation{:with           person-edna
                                   :open-chats     [chat-with-edna]
                                   :previous-chats [chat-with-edna]}])

(def html-example "<h1>Hallo Luca ❤️</h1>")

(def css-example "body { background: red};")

(def html-project
  #:project{:id       (random-uuid)
            :name     "Webseite mit HTML"
            :tags     [:html :website]
            :chapters [#:project.chapter{:id                 (random-uuid)
                                         :name                "Rechtschreibfehler korrigieren"
                                         :notes              ["Fehler korrigieren"
                                                              "Witten nicht Wittne"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    ""
                                                                           :snippet ""
                                                                           :line    "<h1>Wittne</h1>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    ""
                                                                           :snippet ""
                                                                           :line    "body { background: red;}"}}
                                         :story              chat-with-edna
                                         :story-final-action "/app/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Hintergrundfarbe anpassen"
                                         :notes              ["Hintergrundfarbe anpassen"
                                                              "lol"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    ""
                                                                           :snippet ""
                                                                           :line    "<h1>Witten</h1>
                                                                           <p>Wo ist das Murmeltier?</p>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    ""
                                                                           :snippet ""
                                                                           :line    "body { background: yellow;}"}}
                                         :story              chat-with-edna
                                         :story-final-action "/app/editor"}]})

