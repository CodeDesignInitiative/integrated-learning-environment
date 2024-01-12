(ns ile.story.view
  (:require [ile.core.persistence :as persistence]
            [ile.dictonary.translations :as tr]))

(def worlds [{:name     "HTML & CSS"
              :subtitle "Einstieg"
              :target   "html-css"}
             {:name     "JavaScript"
              :subtitle "Fortgeschritten"
              :target   "js"}
             {:name     "Spiele"
              :subtitle "Fortgeschritten"
              :target   "games"}])

(defn learning-tracks-page [lang]
  (let [learning-tracks (persistence/find-all-active-learning-tracks-for-language lang)]
    [:<>
     [:nav
      [:a.button {:href (tr/url lang "/")} "Zurück"]]
     [:main#world-page
      (map
        (fn [{:learning-track/keys [name description]
              :xt/keys             [id]}]
          [:a.tile#world-tile
           {:href (tr/url lang "/world/" id)}
           [:h3 description]
           [:h2 name]
           ])
        learning-tracks)]]))

(defn learning-track-tasks-page [lang
                                 {:learning-track/keys [name description]
                                  :as                  learning-track}
                                 learning-track-tasks]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/worlds")} "Zurück"]
    [:div
     [:h1 name]
     [:p description]]
    [:div]]
   [:main
    [:ol
     (map
       (fn [{:learning-track-task/keys [name]
             :xt/keys                  [id]}]
         [:li [:a {:href (tr/url lang "/world/" (:xt/id learning-track) "/mission/" id)}
               name]])
       learning-track-tasks)]]])

(defn finished-world-page [lang]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/worlds")} "Zurück zur Übersicht"]
    [:a.button {:href (tr/url lang "/projekte")} "Freier Editor"]]
   [:main#finished-world-page
    [:h1 "🎉 Fertig 🎉"]
    [:p "Du hast die verfügbaren Mission durchgespielt."]
    [:p "Bald geht es weiter mit neuen Inhalten."]
    [:h2 "Nutze den freien Editor"]
    [:p "Hier kannst du dich austoben und testen was du gelernt hast"]
    [:a.button {:href (tr/url lang "/projekte")} "Freier Editor"]]]
  )