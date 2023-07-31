(ns ile.story.view
  (:require [ile.dictonary.translations :as tr]))

(def worlds [{:name     "HTML & CSS"
              :subtitle "Einstieg"
              :target   "html-css"}
             {:name     "JavaScript"
              :subtitle "Fortgeschritten"
              :target   "js"}
             {:name     "Spiele"
              :subtitle "Fortgeschritten"
              :target   "games"}])

(defn worlds-page [lang]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/")} "Zurück"]]
   [:main#world-page
    (for [{:keys [name subtitle target]} worlds]
      [:a.tile#world-tile
       {:href (tr/url lang "/world/map/" target)}
       [:h3 subtitle]
       [:h2 name]
       ])]])

(defn world-map-page [lang {:keys [name subtitle] :as _world} levels]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/worlds")} "Zurück"]
    [:div
     [:h2 subtitle]
     [:h1 name]]
    [:div]]
   [:main#world-map-page
    (for [idx (range (count levels))]
      (let [{:mission/keys [name]
             :xt/keys      [id]} (get levels idx)]
        [:a {:href (tr/url lang "/world/mission/" id)}
         [:div
          [:.map-info [:h3 name]]
          [:.map {:class (str "bg" (+ (mod idx 4) 1))}
           [:.level-number (+ idx 1)]]]
         (when (< (+ idx 1) (count levels))
           [:img.arrow {:src "/img/map/i2i_arrow.svg"}])]))]])

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