(ns cd.ile.courses.html-website)

(def html-base "<nav>\n    <span>Der Ruhrgebiets Blog</span>\n</nav>\n\n<main>\n $$placeholder$$ \n</main>\n\n<footer>\n    <ul>\n        <li><a href=\"https://code.design\">Impressum</a></li>\n        <li><a href=\"https://code.design\">Datenschutzerklärung</a></li>\n        <li><a href=\"https://code.design\">Kontakt</a></li>\n    </ul>\n</footer>")

(def css-website "/* Webseite Design */\n\n/* body\n   - wirkt sich auf die ganze Seite auf */\nbody {\n    /* hintergrund */\n    background-color: #fee6e6;\n    background-attachment: fixed;\n    background-image: url(http://localhost:8080/img/courses/ruhr_hintergrund.jpg);\n    background-repeat: no-repeat;\n    background-size: cover;\n\n    /* größe und abstände */\n    min-height: 100vh;\n    padding: 0;\n    margin: 0;\n\n    /* schrift global */\n    font-family: 'Lucida Sans', 'Lucida Sans Regular', sans-serif;\n    line-height: 1.5;\n}\n\n/* links\n   - a steht für anchor (deutsch: Anker) */\na {\n    /* link text farbe */\n    color: #3fa138;\n}\n\n/*\n   ---------------------- navigationsleiste (oben) ----------------------\n*/\n\nnav {\n    /* aussehen */\n    background-color: #c2a07e;\n    box-shadow: 0 4px 12px #0004;\n    padding: 12px 16px;\n\n    /* anordnung */\n    display: flex;\n    gap: 16px;\n    list-style-type: none;\n\n    /* schrift */\n    color: white;\n    text-transform: uppercase;\n    font-weight: bold;\n}\n\n/* erstes Element in der Navigationsleiste */\nnav > span:first-child {\n    /* fülle den verfügbaren Platz aus und\n       schiebe die restlichen Elemente nach rechts */\n    flex-grow: 1;\n}\n\n/* alle Links, die in der Navigationsleiste sind */\nnav a {\n    /* sollen weiß sein */\n    color: white;\n    /* und nicht unterstrichen */\n    text-decoration: none;\n}\n\n/*\n   ---------------------- seiten inhalt ----------------------\n*/\n\nmain {\n    max-width: 800px;\n    padding: 16px;\n    margin: 0 auto;\n}\n\n/* ein blog artikel */\narticle {\n    background-color: rgb(240, 231, 220);\n    border-radius: 12px;\n    box-shadow: 0 4px 24px #0004;\n    padding: 16px;\n    margin-bottom: 64px;\n}\n\n/* alle bilder, die in einem blog artikel sind */\narticle img {\n    width: 300px;\n    height: 300px;\n    border-radius: 200px;\n    object-fit: cover;\n    float: right;\n    box-shadow: 0 0 12px #0004;\n}\n\n/*\n   ---------------------- fußleiste (unten) ----------------------\n*/\n\nfooter {\n    background-color: #c2a07e;\n    padding: 32px 16px;\n\n    /* schrift */\n    font-family: sans-serif;\n    font-weight: 100;\n    color: #54371b;\n    line-height: 2;\n}\n\nfooter > ul {\n    list-style-type: symbols(cyclic \"\uD83D\uDCDC\" \"\uD83D\uDCD1\" \"☎️\");\n    margin: 0;\n}\n\n/* links, die nur im footer sind */\nfooter a {\n    color: #54371b;\n    text-decoration: none;\n}")

(def person-edna #:person{:id           (random-uuid)
                          :name         "Edna"
                          :picture      "/img/persons/avatar_2.png"
                          :organization "Ausbruchs GmbH"})


(def start-chat-with-edna
  [#:story{:person         person-edna
           :message        "Hallo! Du bist unser neustes Teammitglied, oder?"
           :answer-choices ["Korrekt"
                            "Genau"
                            "Weiß nicht"]}
   #:story{:person  person-edna
           :message "Alles klar. Ich habe eine Aufgabe, wo ich Hilfe brauche mit unserer Webseite."}
   #:story{:person         person-edna
           :message        "Kannst du mir da helfen mit dem HTML Code?"
           :answer-choices ["Weiß nicht"
                            "Ein bisschen"
                            "Aber hallo!"]}
   #:story{:person  person-edna
           :message "Mir wurde gesagt, dass unser Code Editor dir bei allem weiteren helfen kann – egal wie viel du schon weisst."}]
  #_#:chat{:final-action "/app/editor?job=website1"
         :conversation [#:story{:person         person-edna
                                :message        "Hallo! Du bist unser neustes Teammitglied, oder?"
                                :answer-choices ["Korrekt"
                                                 "Genau"
                                                 "Weiß nicht"]}
                        #:story{:person  person-edna
                                :message "Alles klar. Ich habe eine Aufgabe, wo ich Hilfe brauche mit unserer Webseite."}
                        #:story{:person         person-edna
                                :message        "Kannst du mir da helfen mit dem HTML Code?"
                                :answer-choices ["Weiß nicht"
                                                 "Ein bisschen"
                                                 "Aber hallo!"]}
                        #:story{:person  person-edna
                                :message "Mir wurde gesagt, dass unser Code Editor dir bei allem weiteren helfen kann – egal wie viel du schon weisst."}]})



(def chat-with-edna-2 [#:story{:person  person-edna
                               :message "Super, vielen Dank für deine Unterstützung! Ich bin wirklich froh, dass du jetzt im Team bist!"}
                       #:story{:person         person-edna
                               :message        "Du hast die letzte Aufgabe so schnell und gut bewältigt, deshalb wollte ich dich fragen, ob du dir nochmal Zeit für die weiteren Anpassungen nehmen kannst?"
                               :answer-choices ["Gerne"
                                                "Später"
                                                "Das mache ich direkt"]}
                       #:story{:person  person-edna
                               :message "Okay, das freut mich zu hören. Norma von der Marketingabteilung hat noch einige Fehler auf der Webseite gefunden. Ich habe dir den korrigierten Absatz auf deinen Notizzettel aufgeschrieben"}])

(def html-1-snippet "\n    <article>\n        <b>Vereine</b>\n        <img src=\"http://localhost:8080/img/courses/ruhr_hintergrund.jpg\">\n        $$placeholder$$        <ul>\n            <li>\uD83E\uDDCD Neue Leute treffen</li>\n            <li>\uD83E\uDD51 Tolle Events</li>\n            <li>\uD83D\uDD04 Jede Woche</li>\n        </ul>\n\n        <h3>Veranstaltungen</h3>\n        <p>\n            Jede Woche findet in \"Der Werkstatt\" ein offenes Treffen für\n            Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n        </p>\n\n        <p>\n            Es werden auch Ausflüge organisiert und du kannst die geplanten\n            Events jederzeit auf der Webseite\n            <a href=\"https://signalofyouth.de/\"\n               target=\"_blank\">\n                signalofyouth.de ↗️\n            </a>\n            einsehen!\n        </p>\n    </article>")
(def html-1-line "<h2>SoYou, Wittne</h2>")

(def html-2-snippet "\n    <article>\n        <b>Vereine</b>\n        <img src=\"http://localhost:8080/img/courses/ruhr_hintergrund.jpg\">\n        <h2>SoYou, Witten</h2>        <ul>\n            <li>\uD83E\uDDCD Neue Leute treffen</li>\n            <li>\uD83E\uDD51 Tolle Events</li>\n            <li>\uD83D\uDD04 Jede Woche</li>\n        </ul>\n\n        <h3>Veranstaltungen</h3>\n       $$placeholder$$\n\n        <p>\n            Es werden auch Ausflüge organisiert und du kannst die geplanten\n            Events jederzeit auf der Webseite\n            <a href=\"https://signalofyouth.de/\"\n               target=\"_blank\">\n                signalofyouth.de ↗️\n            </a>\n            einsehen!\n        </p>\n    </article>")
(def html-2-line "<p>\n Jede Wohce findet in \"Der \n Werkstat\" ein offnes Treffen \n für Jugendliche satt. \n Hier könnnen alle teil nehmen, \n die Lust haben. \n</p>")

(def html-project
  #:project{:id       "website1"
            :name     "Webseite mit HTML"
            :tags     [:html :website]
            :chapters [#:project.chapter{:id                 (random-uuid)
                                         :name               "Rechtschreibfehler korrigieren"
                                         :notes              ["Fehler korrigieren"
                                                              "Witten nicht Wittne"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-1-snippet
                                                                           :line    html-1-line}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              start-chat-with-edna
                                         :story-final-action "/app/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Rechtschreibfehler korrigieren 2x§"
                                         :notes              ["Korrektur:"
                                                              "Jede Woche findet in \"Der \n Werkstatt\" ein offenes Treffen \n für Jugendliche statt. \n Hier können alle teilnehmen, \n die Lust haben. "]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-2-snippet
                                                                           :line    html-2-line}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-edna-2
                                         :story-final-action "/app/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Hintergrundfarbe anpassen"
                                         :notes              ["Hintergrundfarbe anpassen"
                                                              "lol"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    ""
                                                                           :snippet ""
                                                                           :line    "<h1>Witten</h1>\n<p>Wo ist das Murmeltier?</p>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    ""
                                                                           :snippet ""
                                                                           :line    "body { background: yellow;}"}}
                                         :story              chat-with-edna-2
                                         :story-final-action "/app/editor"}]})


(comment
  (get (:project/chapters html-project) 0)
  )