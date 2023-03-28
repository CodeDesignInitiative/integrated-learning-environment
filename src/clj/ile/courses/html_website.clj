(ns ile.courses.html-website)

(def html-base "<nav>\n    <span>Der Ruhrgebiets Blog</span>\n</nav>\n\n<main>\n $$placeholder$$ \n</main>\n\n<footer>\n    <ul>\n        <li><a href=\"https://code.design\">Impressum</a></li>\n        <li><a href=\"https://code.design\">Datenschutzerklärung</a></li>\n        <li><a href=\"https://code.design\">Kontakt</a></li>\n    </ul>\n</footer>")

(def css-website "/* Webseite Design */\n\n/* body\n   - wirkt sich auf die ganze Seite auf */\nbody {\n    /* hintergrund */\n    background-color: #fee6e6;\n    background-attachment: fixed;\n    background-image: url(https://code-editor.digital/img/courses/ruhr_hintergrund.jpg);\n    background-repeat: no-repeat;\n    background-size: cover;\n\n    /* größe und abstände */\n    min-height: 100vh;\n    padding: 0;\n    margin: 0;\n\n    /* schrift global */\n    font-family: 'Lucida Sans', 'Lucida Sans Regular', sans-serif;\n    line-height: 1.5;\n}\n\n/* links\n   - a steht für anchor (deutsch: Anker) */\na {\n    /* link text farbe */\n    color: #3fa138;\n}\n\n/*\n   ---------------------- navigationsleiste (oben) ----------------------\n*/\n\nnav {\n    /* aussehen */\n    background-color: #c2a07e;\n    box-shadow: 0 4px 12px #0004;\n    padding: 12px 16px;\n\n    /* anordnung */\n    display: flex;\n    gap: 16px;\n    list-style-type: none;\n\n    /* schrift */\n    color: white;\n    text-transform: uppercase;\n    font-weight: bold;\n}\n\n/* erstes Element in der Navigationsleiste */\nnav > span:first-child {\n    /* fülle den verfügbaren Platz aus und\n       schiebe die restlichen Elemente nach rechts */\n    flex-grow: 1;\n}\n\n/* alle Links, die in der Navigationsleiste sind */\nnav a {\n    /* sollen weiß sein */\n    color: white;\n    /* und nicht unterstrichen */\n    text-decoration: none;\n}\n\n/*\n   ---------------------- seiten inhalt ----------------------\n*/\n\nmain {\n    max-width: 800px;\n    padding: 16px;\n    margin: 0 auto;\n}\n\n/* ein blog artikel */\narticle {\n    background-color: rgb(240, 231, 220);\n    border-radius: 12px;\n    box-shadow: 0 4px 24px #0004;\n    padding: 16px;\n    margin-bottom: 64px;\n}\n\n/* alle bilder, die in einem blog artikel sind */\narticle img {\n    width: 300px;\n    height: 300px;\n    border-radius: 200px;\n    object-fit: cover;\n    float: right;\n    box-shadow: 0 0 12px #0004;\n}\n\n/*\n   ---------------------- fußleiste (unten) ----------------------\n*/\n\nfooter {\n    background-color: #c2a07e;\n    padding: 32px 16px;\n\n    /* schrift */\n    font-family: sans-serif;\n    font-weight: 100;\n    color: #54371b;\n    line-height: 2;\n}\n\nfooter > ul {\n    list-style-type: symbols(cyclic \"\uD83D\uDCDC\" \"\uD83D\uDCD1\" \"☎️\");\n    margin: 0;\n}\n\n/* links, die nur im footer sind */\nfooter a {\n    color: #54371b;\n    text-decoration: none;\n}")

(def person-edna #:person{:id           (random-uuid)
                          :name         "Edna"
                          :picture      "/img/persons/avatar_2.png"
                          :organization "Ausbruchs GmbH"})


(def person-mehmet #:person{:id           (random-uuid)
                            :name         "Mehmet"
                            :picture      "/img/persons/avatar_1.png"
                            :organization "Ausbruchs GmbH"})


(def start-chat-with-edna [#:story{:person         person-edna
                                   :message        "Hallo ich bin Edna. Schön, dass du da bist, wir haben uns schon auf deinen ersten Arbeitstag gefreut!"
                                   :answer-choices ["Hi!"]}
                           #:story{:person         person-edna
                                   :message        "Deine erste Aufgabe wartet schon auf dich. Kannst du mir kurz helfen?"
                                   :answer-choices ["Na klar!"]}
                           #:story{:person  person-edna
                                   :message "Wunderbar!"}])

(def chat-with-edna-2 [#:story{:person  person-edna
                               :message "Super, vielen Dank für deine Unterstützung! Ich bin wirklich froh, dass du jetzt im Team bist!"}
                       #:story{:person  person-edna
                               :message "Norma von der Marketingabteilung hat noch einige Fehler auf der Webseite gefunden. Ich hab' dir den korrigierten Absatz nochmal auf einen Notizzettel geschrieben – du kannst ihn einfach kopieren."}])

(def chat-with-edna-3 [#:story{:person  person-edna
                               :message "Wow auf dich ist wirklich Verlass!"}
                       #:story{:person         person-edna
                               :message        "Wilma aus der Produktabteilung hätte gerne noch einen weiteren Stichpunkt in der Auflistung. Dein Vorgänger hat den Stichpunkt bereits angelegt, leider ist er noch nicht dazu gekommen, den Text einzufügen."
                               :answer-choices ["Gerne!"]}
                       #:story{:person  person-edna
                               :message "Danke!"}])

(def chat-with-edna-4 [#:story{:person  person-edna
                               :message "Wilma kann sich echt nicht entscheiden!"}
                       #:story{:person         person-edna
                               :message        "Sie hat mich gerade darum gebeten, den Stichpunkt „Tolle Atmosphäre“ wieder zu entfernen."
                               :answer-choices ["Kein Problem!"]}
                       #:story{:person  person-edna
                               :message "Danke!"}])

(def chat-with-edna-4-1 [#:story{:person         person-edna
                                 :message        "Uns ist aufgefallen, dass das Team von SoYou noch gar nicht erwähnt wurde. Kannst du bitte noch einen kurzen Stichpunkt zum Team hinzufügen?"
                                 :answer-choices ["Na Klar!"]}
                         #:story{:person         person-edna
                                 :message        "Das freut mich :)"}])

(def chat-with-mehmet-5 [#:story{:person  person-mehmet
                                 :message "Wow auf dich ist wirklich Verlass!"}
                         #:story{:person         person-mehmet
                                 :message        "Ich habe eine Bitte an dich. Könntest du bitte die Unterüberschrift „Webseite von SoYou“ erstellen?"
                                 :answer-choices ["Hi Mehmet, ich helfe dir gerne!!"]}
                         #:story{:person  person-edna
                                 :message "Perfekt!"}])

(def chat-with-edna-6 [#:story{:person  person-edna
                               :message "Unsere Freunde von code+design haben uns darum gebeten, einen neuen Blogeintrag zu erstellen, um Werbung für das code+design Camp zu machen."}
                       #:story{:person  person-edna
                               :message "Kannst du bitte schon mal die Überschrift dafür erstellen?"}])

(def chat-with-mehmet-7 [#:story{:person         person-mehmet
                                 :message        "Nora von code+design hat uns eine Nachricht geschrieben und uns darum gebeten einen Link einzufügen, der auf die Webseite von code+design führt."
                                 :answer-choices ["Das kann ich gerne machen."]}
                         #:story{:person  person-mehmet
                                 :message "Ich glaube du bist für diese Aufgabe perfekt geeignet!"}])

(def chat-with-mehmet-8 [#:story{:person  person-mehmet
                                 :message "Es gibt noch einen Link für das aktuelle Event. Dort können sich Kinder und Jugendliche zur Teilnahme anmelden. "}
                         #:story{:person         person-mehmet
                                 :message        "Wir sollten auch diesen Link in dem Blogeintrag zeigen."
                                 :answer-choices ["Alles klar!"]}
                         #:story{:person  person-mehmet
                                 :message "Danke dir!"}])

(def html-1-snippet "\n    <article>\n        <b>Vereine</b>\n        <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n        $$placeholder$$        <ul>\n            <li>\uD83E\uDDCD Neue Leute treffen</li>\n            <li>\uD83E\uDD51 Tolle Events</li>\n            <li>\uD83D\uDD04 Jede Woche</li>\n        </ul>\n\n        <h3>Veranstaltungen</h3>\n        <p>\n            Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n            Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n        </p>\n\n        <p>\n            Es werden auch Ausflüge organisiert und du kannst die geplanten\n            Events jederzeit auf der Webseite\n            <a href=\"https://signalofyouth.de/\"\n               target=\"_blank\">\n                signalofyouth.de ↗️\n            </a>\n            einsehen!\n        </p>\n    </article>")
(def html-1-line "<h2>SoYou, Wittne</h2>")

(def html-2-snippet "\n    <article>\n        <b>Vereine</b>\n        <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n        <h2>SoYou, Witten</h2>        <ul>\n            <li>\uD83E\uDDCD Neue Leute treffen</li>\n            <li>\uD83E\uDD51 Tolle Events</li>\n            <li>\uD83D\uDD04 Jede Woche</li>\n        </ul>\n\n        <h3>Veranstaltungen</h3>\n       $$placeholder$$\n\n        <p>\n            Es werden auch Ausflüge organisiert und du kannst die geplanten\n            Events jederzeit auf der Webseite\n            <a href=\"https://signalofyouth.de/\"\n               target=\"_blank\">\n                signalofyouth.de ↗️\n            </a>\n            einsehen!\n        </p>\n    </article>")
(def html-2-line "<p>\n Jede Wohce findet in \"Der \n Werkstat\" ein offnes Treffen \n für Jugendliche satt. \n Hier könnnen alle teil nehmen, \n die Lust haben. \n</p>")

(def html-3-snippet "\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        $$placeholder$$<li>\uD83E\uDDCD Neue Leute treffen</li>\n        <li>\uD83E\uDD51 Tolle Events</li>\n        <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n    <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-3-line "<li>\uD83C\uDFE1 </li>\n")

(def html-4-snippet "\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        <li>\uD83C\uDFE1 Meetups jeden Monat</li>\n<li>\uD83E\uDDCD Neue Leute treffen</li>\n   $$placeholder$$     <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n     <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-4-line "<li>\uD83E\uDD51 Tolle Events</li>")

(def html-4-1-snippet "\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        <li>\uD83C\uDFE1 Meetups jeden Monat</li>\n<li>\uD83E\uDDCD Neue Leute treffen</li>\n   $$placeholder$$     <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n     <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-4-1-line "")

(def html-5-snippet "\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        <li>\uD83C\uDFE1 Meetups jeden Monat</li>\n<li>\uD83E\uDDCD Neue Leute treffen</li>\n        <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n  $$placeholder$$  <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-5-line "<h3></h3>\n")

(def html-6-snippet "$$placeholder$$\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        <li>\uD83C\uDFE1 Meetups jeden Monat</li>\n<li>\uD83E\uDDCD Neue Leute treffen</li>\n        <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n  <h3>Webseite von SoYou</h3>\n  <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-6-line "<article>\n    <h2>     </h2>\n</article>\n")

(def html-6-1-snippet "$$placeholder$$\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        <li>\uD83C\uDFE1 Meetups jeden Monat</li>\n<li>\uD83E\uDDCD Neue Leute treffen</li>\n        <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n  <h3>Webseite von SoYou</h3>\n  <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-6-1-line "<article>\n    <h2>     </h2>\n</article>\n")

(def html-7-snippet "<article>\n    <h2>code+design Camp Mai 2023</h2>\n$$placeholder$$\n</article>\n\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        <li>\uD83C\uDFE1 Meetups jeden Monat</li>\n<li>\uD83E\uDDCD Neue Leute treffen</li>\n        <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n  <h3>Webseite von SoYou</h3>\n  <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-7-line "")

(def html-8-snippet "<article>\n    <h2>code+design Camp Mai 2023</h2>\n<a href=\"https://code.design\">code+design</a><br>\n$$placeholder$$\n</article>\n<article>\n    <b>Vereine</b>\n    <img src=\"https://code-editor.digital/img/courses/ruhr_hintergrund.jpg\">\n    <h2>SoYou, Witten</h2>\n    <ul>\n        <li>\uD83C\uDFE1 Meetups jeden Monat</li>\n<li>\uD83E\uDDCD Neue Leute treffen</li>\n        <li>\uD83D\uDD04 Jede Woche</li>\n            </ul>\n\n    <h3>Veranstaltungen</h3>\n    <p>\n        Jede Woche findet in \"Der Werkstadt\" ein offenes Treffen für\n        Jugendliche satt. Hier können alle teilnehmen, die Lust haben.\n    </p>\n\n  <h3>Webseite von SoYou</h3>\n  <p>\n        Es werden auch Ausflüge organisiert und du kannst die geplanten\n        Events jederzeit auf der Webseite\n        <a href=\"https://signalofyouth.de/\"\n           target=\"_blank\">\n            signalofyouth.de ↗️\n        </a>\n        einsehen!\n    </p>\n</article>")
(def html-8-line "")

(def html-project
  #:project{:id       "website1"
            :name     "Webseite mit HTML"
            :tags     [:html :website]
            :chapters [#:project.chapter{:id                 (random-uuid)
                                         :name               "Rechtschreibfehler korrigieren"
                                         :notes              ["Jemand hat sich bei der Überschrift vertippt. Es müsste \"Witten\" heißen."
                                                              "Korrigiere den Rechtschreibfehler im Code-Fenster über dieser Notiz."]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-1-snippet
                                                                           :line    html-1-line
                                                                           :answer  "<h2>SoYou, Witten</h2>"
                                                                           }
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              start-chat-with-edna
                                         :story-final-action "/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Rechtschreibfehler korrigieren"
                                         :notes              ["Suche den Rechtschreibfehler, oder Markiere den gesamten Text:"
                                                              "Jede Woche findet in \"Der \n Werkstatt\" ein offenes Treffen \n für Jugendliche statt. \n Hier können alle teilnehmen, \n die Lust haben."
                                                              "Korrigiere den Fehler im Text oder füge den markierten Text zwischen den sogenannten \"Tags\" <p> HIER TEXT EINFÜGEN </p> ein."]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-2-snippet
                                                                           :line    html-2-line
                                                                           :answer  "<p>Jede Woche findet in \"Der \n Werkstatt\" ein offenes Treffen \n für Jugendliche statt. \n Hier können alle teilnehmen, \n die Lust haben.</p>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-edna-2
                                         :story-final-action "/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Stichpunkt hinzufügen"
                                         :notes              ["Schreibe einen neuen Stichpunkt, indem du den Text \"Meetups jeden Monat\" in den \"Tag\" für Stichpunkte einfügst:"
                                                              "<li> HIER DEN TEXT EINFÜGEN </li>"
                                                              "\uD83C\uDFE1 kopiere hier das Emoji,falls du es aus Versehen entfernt hast"
                                                              "LG Edna"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-3-snippet
                                                                           :line    html-3-line
                                                                           :answer  "<li>\uD83C\uDFE1 Meetups jeden Monat</li>\n"}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-edna-3
                                         :story-final-action "/editor"}



                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Stichpunkt entfernen"
                                         :notes              ["Entferne den Stichpunkt \"Tolle Events\" indem du den Text markierst und löschst"
                                                              "Entferne auch den \"Tag\" <li> und </li>, sowie das Emoji"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-4-snippet
                                                                           :line    html-4-line
                                                                           :answer  ""}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-edna-4
                                         :story-final-action "/editor"}


                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Stichpunkt entfernen"
                                         :notes              ["Schreibe einen Stichpunkt für das Team von SoYou \"Tolles Team\""
                                                              "Erinnerst du dich noch daran, wie der \"Tag\" für eine Aufzählung geht? - Probiert mal aus!"
                                                              "Wenn du wirklich nicht weiter weißt drück' einfach auf die Glühbirne"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-4-1-snippet
                                                                           :line    html-4-1-line
                                                                           :answer  "<li>Tolles Team</li>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-edna-4-1
                                         :story-final-action "/editor"}



                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Überschrift hinzufügen"
                                         :notes              ["Überschrift \"<h3>Webseite von SoYou</h3>\" hinzufügen"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-5-snippet
                                                                           :line    html-5-line
                                                                           :answer  "<h3>Webseite von SoYou</h3>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-mehmet-5
                                         :story-final-action "/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Neuer Artikel Überschrift hinzufügen"
                                         :notes              ["Überschrift \"code+design Camp Mai 2023\" hinzufügen"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-6-snippet
                                                                           :line    html-6-line
                                                                           :answer  "<article><h2>code+design Camp Mai 2023</h2></article>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-edna-6
                                         :story-final-action "/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Link hinzufügen"
                                         :notes              ["Füge einen Link zur Webseite von code+design ein"
                                                              "Einen Link erstellst du mit dem \"Tag\":"
                                                              "<a href=\"HIER ADRESSE EINFÜGEN\">NAME DES LINKS</a>"
                                                              "Die Adresse ist \"https://code.design\""
                                                              "Der Name vom Link soll \"code+design\""]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-7-snippet
                                                                           :line    html-7-line
                                                                           :answer  "<a href=\"https://code.design\">code+design</a>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-mehmet-7
                                         :story-final-action "/editor"}
                       #:project.chapter{:id                 (random-uuid)
                                         :name               "Neuen Link hinzufügen"
                                         :notes              ["Füge den Link für die Events Seite von code+design in den Blogeintrag ein"
                                                              "Der Link ist \"www.code.design/events/2302ruhr\" und soll unter dem Namen \"Am Camp teilnehmen\" angezeigt werden"
                                                              "Erinnere dich an den Aufbau eines Links: <a href=\"ADRESSE DER WEBSEITE (zum Beispiel \"www.google.de\")\">name des Links (zum Beispiel \"Google\")</a>"]
                                         :code               {:html #:code{:lang    :html
                                                                           :base    html-base
                                                                           :snippet html-8-snippet
                                                                           :line    html-8-line
                                                                           :answer  "<a href=\"www.code.design/events/2302ruhr\">Code Camp 2302</a>"}
                                                              :css  #:code{:lang    :css
                                                                           :base    css-website
                                                                           :snippet ""
                                                                           :line    ""}}
                                         :story              chat-with-mehmet-8
                                         :story-final-action "/editor"}]})
