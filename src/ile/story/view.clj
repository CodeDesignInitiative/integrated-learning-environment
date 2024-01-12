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
   [:main.p3
    [:h2 "Kapitel"]
    (if (not-empty learning-track-tasks)
      [:ol.col.gap
       (map
         (fn [{:learning-track-task/keys [name]
               :xt/keys                  [id]}]
           [:li [:a.button {:href (tr/url lang "/world/" (:xt/id learning-track) "/mission/" id)}
                 name]])
         learning-track-tasks)]
      [:<>
       [:p "Hups. Hier gibt es noch nichts zu tun. Schau dir erstmal die anderen Stories an!"]
       [:a.button {:href (tr/url lang "/worlds")} "Zurück"]]
      )]])

(defn finished-world-page [lang]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/worlds")} "Zurück zur Übersicht"]
    [:a.button {:href (tr/url lang "/projekte")} "Freier Editor"]]
   [:main#finished-world-page
    [:h1 "🎉 Fertig 🎉"]
    [:p "Du hast diese Welt durchgespielt."]
    [:p "Schaue dir andere Themen und Welten an, oder"]
    [:h2 "Nutze den freien Editor"]
    [:p "Hier kannst du dich austoben und testen was du gelernt hast"]
    [:a.button {:href (tr/url lang "/projekte")} "Freier Editor"]]]
  )