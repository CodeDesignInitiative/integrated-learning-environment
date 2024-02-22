(ns ile.story.view
  (:require [ile.core.persistence :as persistence]
            [ile.dictonary.translations :as tr]
            [markdown.core :as md]))

(def worlds [{:name     "HTML & CSS"
              :subtitle "Einstieg"
              :target   "html-css"}
             {:name     "JavaScript"
              :subtitle "Fortgeschritten"
              :target   "js"}
             {:name     "Spiele"
              :subtitle "Fortgeschritten"
              :target   "games"}])

(defn learning-tracks-page [lang mode]
  (let [learning-tracks (if (= mode :story)
                          (persistence/find-all-active-story-tracks-for-language lang)
                          (persistence/find-all-active-learning-tracks-for-language lang))]
    [:<>
     [:header
      [:nav.p3
       [:a.button {:href (tr/url lang "/")} "Zurück"]]]
     [:main#world-page
      (map
        (fn [{:learning-track/keys [name description]
              :xt/keys             [id]}]
          [:a.tile#world-tile
           {:href (tr/url lang (if (= mode :story) "/world/" "/learning-track/") id)}
           [:h3 description]
           [:h2 name]
           ])
        learning-tracks)]]))

(defn learning-track-tasks-page [lang
                                 {:learning-track/keys [name description]
                                  :as                  learning-track}
                                 learning-track-tasks
                                 story-mode?]
  [:<>
   [:header.p3
    [:nav
     [:a.button {:href (tr/url lang (if story-mode? "/worlds" "/learn"))} "Zurück"]
     [:div
      [:h1 name]
      [:p description]]
     [:div]]]
   [:main.p3
    [:h2 "Kapitel"]
    (if (not-empty learning-track-tasks)
      [:ol.col.gap
       (map
         (fn [{:learning-track-task/keys [name]
               :xt/keys                  [id]}]
           [:li [:a.button {:href (tr/url lang (if story-mode?
                                                 (str "/world/" (:xt/id learning-track) "/mission/")
                                                 (str "/learning-track/" (:xt/id learning-track) "/task/")) id)}
                 name]])
         learning-track-tasks)]
      [:<>
       [:p "Hups. Hier gibt es noch nichts zu tun. Schau dir erstmal die anderen Stories an!"]
       [:a.button {:href (tr/url lang "/worlds")} "Zurück"]]
      )]])

(defn finished-world-page [lang {:learning-track/keys [concluding-message]}]
  [:<>
   [:header.p3
    [:nav
     [:a.button {:href (tr/url lang "/")} "Zurück zum Start"]
     [:a.button {:href (tr/url lang "/learn")} "Kursübersicht"]]]
   [:main#finished-world-page.content
    [:h1 "🎉 Fertig 🎉"]

    (or [:div {:dangerouslySetInnerHTML {:__html (md/md-to-html-string concluding-message)}}]
        [:<>
         [:p "Du hast diese Welt durchgespielt."]
         [:p "Schaue dir andere Themen und Welten an, oder"]])

    [:h2 "Nutze den freien Editor"]
    [:p "Hier kannst du dich austoben und testen was du gelernt hast"]
    [:a.button {:href (tr/url lang "/projekte")} "Freier Editor"]]]
  )