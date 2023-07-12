(ns ile.story.view
  (:require [ile.dictonary.translations :as tr]))

(def worlds [{:name "HTML & CSS"
              :subtitle "Einstieg"
              :target "html-css"}
             {:name "JavaScript"
              :subtitle "Fortgeschritten"
              :target "js"}
             {:name "Spiele"
              :subtitle "Fortgeschritten"
              :target "games"}])

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
    [:a.button {:href (tr/url lang "/worlds")} "Zurück"]]
   [:main#world-map-page
    [:h1 name]
    [:h2 subtitle]
    (for [{:mission/keys [name ]
           :xt/keys [id]} levels]
      [:a.button {:href (tr/url lang "/world/mission/" id)} name])]])

(defn mission-editor-page
  [lang {:mission/keys [world step story-before
                        story-after content] :as mission}]
  (clojure.pprint/pprint mission)
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/world/map/" (name world))} "Zurück"]
    [:h3 (:mission/name mission)]]
   [:main#mission-editor-page

    ]])