(ns ile.ui.start.view
  (:require
    [ile.dictonary.translations :as tr]))


(defn start-screen [lang is-admin? is-teacher?]
  [:main#start-screen
   [:div#start-button-row
    [:div#title
     [:img.logo
      {:src "/img/logo_play_learn.svg"
       :alt "App Icon mit Aufschrift: play + learn"}]]
    [:div
     (when (or is-teacher? is-admin?)
       [:a.button
        {:href "/admin"}
        "Teacher Area"])
     [:a.button
      {:href "/ru/"}
      [:img {:src "/img/icons/language.svg"}]
      (tr/tr lang :start/language-btn)]
     [:a.button
      {:href "/logout"}
      (tr/tr lang :login/logout)]
     [:a.button {:href  "/info"
                 :title "Info Page"}
      [:img {:src "/img/icons/info.svg"
             :alt "Info icon: an i within a circle"}]]]]

   [:a.tile#story-area
    {:href (tr/url lang "/worlds")}
    [:img#story-peep
     {:src "/img/peeps/Chaotic Good.svg"
      :alt "Drawn person with a astronaut hemlet."}]
    [:img.icon
     {:src "/img/icons/map-outline.svg"
      :alt "Karten Icon"}]
    [:h2 (tr/tr lang :start/story-tile)]]

   [:a.tile#editor-area
    {:href (tr/url lang "/projekte")}
    [:img#editor-peep
     {:src "/img/peeps/Experiments.svg"
      :alt "Drawn person with a astronaut hemlet."}]
    [:img.icon
     {:src "/img/icons/code.svg"
      :alt "Code Symbol: eckige Klammern, die einen Schrägstrich umschließen"}]
    [:h2 (tr/tr lang :start/editor-tile)]]])