(ns ile.ui.start.view
  (:require
    [ile.util :as util]
    [rum.core :as rum]))


(defn start-screen [lang]
  [:main#start-screen
   [:div#title
    [:img.logo
     {:src "/img/logo_play_learn.svg"
      :alt "App Icon mit Aufschrift: play + learn"}]]
   [:a.tile#login-area
    {:href (util/lang-url lang "/login")}
    [:img.icon
     {:src "/img/icons/happy-outline.svg"
      :alt "Happy emoji Icon"}]
    [:h2 "Anmelden"]
    ]
   [:a.tile#story-area
    [:img#story-peep
     {:src "/img/peeps/story_peep.svg"
      :alt "Drawn person standing holding a laptop."}]
    [:img.icon
     {:src "/img/icons/map-outline.svg"
      :alt "Karten Icon"}]
    [:h2 "Story"]]
   [:a.tile#editor-area
    {:href (util/lang-url lang "/projekte")}
    [:img.icon
     {:src "/img/icons/code.svg"
      :alt "eckige Klammern, die einen Schrägstrich umschließen"}]
    [:h2 "Editor"]
    [:img#peep-laptop
     {:src "/img/peeps/peep_laptop.svg"
      :alt "Person holding a laptop."}]]

   [:a.tile#wiki-area
    [:img.icon
     {:src "/img/icons/bookmark.svg"
      :alt "Lesezeichen Icon"}]
    [:h2 "Wiki"]]
   [:div#start-button-row
    [:a.button
     [:p "Sprache"]]
    [:a.button
     [:p "Hilfe ?!"]]]
   ]
  )