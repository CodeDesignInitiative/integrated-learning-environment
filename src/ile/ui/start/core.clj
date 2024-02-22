(ns ile.ui.start.core
  (:require
    [ile.dictonary.translations :as tr]
    [ile.ui.start.view :as view]))

(defn- start-screen [request]
  (let [lang (tr/lang request)
        logged-in? (some? (get-in request [:session :user]))
        is-admin? (boolean (some #{:admin} (get-in request [:session :user :user/roles])))
        is-teacher? (boolean (some #{:teacher} (get-in request [:session :user :user/roles])))]
    (view/start-screen lang logged-in? is-admin? is-teacher?)))

(defn- info-screen [request]
  [:<>
   [:nav
    [:a.button {:href "/"}
     "Zurück"]]
   [:main.m3
    [:h1 "Info"]

    [:ul
     [:li
      [:a.link {:href "https://code.design/impressum"}
       "Impressum"]]
     [:li
      [:a.link {:href "/datenschutz"}
       "Datenschutz"]]]]]
  )

(defn vsc-page [_request]
  [:<>
   [:header.p3
    [:nav
     [:a.button {:href "/"} "Zurück"]]]
   [:main.content
    [:h1 "Wie mache ich weiter?"]
    [:p "Diese Webseite ist nur der Anfang. Wenn du an deinen eigenen, größeren Projekten arbeiten willst,
    gibt es noch ganz andere Wege mit HTML und CSS zu programmieren:"]

    [:ol
     [:li "Lade diese Vorlage herunter: " [:strong [:a {:href "/webseite.zip"} "↓ Vorlage Webseite herunterladen"]]]
     [:li [:p "Speicher sie unter \"Dokumente\""]]
     [:li
      [:p "Visual Studio Code starten: Shortcut auf Desktop doppelklicken"]
      [:img {:src "/img/vsc/vsc_shortcut.png"}]]
     [:li
      [:p "Seitenleiste einblenden"]
      [:img.vsc-image {:src "/img/vsc/vsc_seitenleiste.png"}]]
     [:li
      [:p "\"Open Folder\" Knopf drücken"]
      [:img.vsc-image {:src "/img/vsc/vsc_ordner_oeffnen.png"}]]
     [:li "Dokumente Ordner öffnen"]
     [:li "\"webseite\" Ordner auswählen (einmal Klicken) und unten rechts auf den \"Ordner öffnen\" Knopf drücken"]
     [:li "Los legen!"]]]])

(def routes
  [""
   ["/:lang/" {:get start-screen}]
   ["/vsc" {:get vsc-page}]
   ["/info" {:get info-screen}]])
