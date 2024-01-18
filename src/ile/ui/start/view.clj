(ns ile.ui.start.view
  (:require
    [ile.dictonary.translations :as tr]))


(defn start-screen [lang logged-in? is-admin? is-teacher?]
  [:main#start-screen.col.gap-l.p3
   [:nav.row
    [:img.logo
     {:src "/img/logo_play_learn.svg"
      :alt "App Icon mit Aufschrift: play + learn"}]
    [:menu.row.gap.wrap
     (when (or is-teacher? is-admin?)
       [:a.button
        {:href "/en/admin"}
        "Teacher Area"])
     [:a.button
      {:href "/ru/"}
      [:img {:src "/img/icons/language.svg"}]
      (tr/tr lang :start/language-btn)]
     (if logged-in?
       [:a.button
        {:href "/logout"}
        (tr/tr lang :login/logout)]
       [:a.button
        {:href "/login"}
        (tr/tr lang :login/login)])
     [:a.button {:href  "/info"
                 :title "Info Page"}
      [:img {:src "/img/icons/info.svg"
             :alt "Info icon: an i within a circle"}]
      "Info"]]]

   [:#modes.row.gap-l.wrap
    [:a.tile#story-area
     {:href (tr/url lang "/worlds")}
     [:img.icon
      {:src "/img/peeps/Chaotic Good.svg"
       :alt "Drawn person with a astronaut hemlet."}]
     #_[:img.icon
        {:src "/img/icons/map-outline.svg"
         :alt "Karten Icon"}]
     [:h2 "Play" #_(tr/tr lang :start/story-tile)]]

    [:a.tile#learn-area
     {:href (tr/url lang "/learn")}
     [:img.icon
      {:src "/img/peeps/Growth.svg"
       :alt "Drawn person with a astronaut hemlet."}]
     #_[:img.icon
        {:src "/img/icons/book-outline.svg"
         :alt "Karten Icon"}]
     [:h2 "Learn " #_(tr/tr lang :start/story-tile)]]

    [:a.tile#editor-area
     {:href (tr/url lang "/projekte")}
     [:img.icon
      {:src "/img/peeps/Experiments.svg"
       :alt "Drawn person with a astronaut hemlet."}]
     #_[:img.icon
        {:src "/img/icons/code.svg"
         :alt "Code Symbol: eckige Klammern, die einen Schrägstrich umschließen"}]
     [:h2 "Code" #_(tr/tr lang :start/editor-tile)]]]])