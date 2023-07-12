(ns ile.ui.start.view
  (:require
    [ile.dictonary.translations :as tr]))


(defn start-screen [lang]
  [:main#start-screen
   [:div#title
    [:img.logo
     {:src "/img/logo_play_learn.svg"
      :alt "App Icon mit Aufschrift: play + learn"}]]
   [:a.tile#login-area
    {:href (tr/url lang "/login")}
    [:img.icon
     {:src "/img/icons/happy-outline.svg"
      :alt "Happy emoji Icon"}]
    [:h2 (tr/tr lang :login/login)]]
   [:a.tile#story-area
    {:href (tr/url lang "/worlds")}
    [:img#story-peep
     {:src "/img/peeps/story_peep.svg"
      :alt "Drawn person standing holding a laptop."}]
    [:img.icon
     {:src "/img/icons/map-outline.svg"
      :alt "Karten Icon"}]
    [:h2 (tr/tr lang :start/story-tile)]]
   [:a.tile#editor-area
    {:href (tr/url lang "/projekte")}
    [:img.icon
     {:src "/img/icons/code.svg"
      :alt "eckige Klammern, die einen Schrägstrich umschließen"}]
    [:h2 (tr/tr lang :start/editor-tile)]
    [:img#peep-laptop
     {:src "/img/peeps/peep_laptop.svg"
      :alt "Person holding a laptop."}]]

   [:a.tile#wiki-area
    [:img.icon
     {:src "/img/icons/bookmark.svg"
      :alt "Lesezeichen Icon"}]
    [:h2 (tr/tr lang :start/wiki-tile)]]
   [:div#start-button-row
    [:a.button
     [:img {:src "/img/icons/language.svg"}]
     (tr/tr lang :start/language-btn)]
    [:a.button
     (tr/tr lang :start/help-btn)]]])