(ns ile.ui.editor.view
  (:require [ile.ui.components :as components]
            [ile.dictonary.translations :as tr]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]])
  (:import (java.time LocalTime)
           (java.time.format DateTimeFormatter)))

(defn- editor-tabs
  "The editor tab bar"
  []
  [:div#editor-tabs
   [:button {:onclick "change_language(\"html\")"}
    "HTML"]
   [:button {:onclick "change_language(\"css\")"}
    "CSS"]])

(defn hidden-code [{:keys [html css]}]
  [:div#hidden-code
   [:#html-base
    html]
   [:#html-snippet
    html]
   [:#html-answer
    (:code/answer html)]
   [:#css-base
    css]])

(defn interaction-buttons [next]
  [:div#editor-interaction
   [:button.circular-button.neutral
    {:onclick "show_help()"}
    [:img {:src   "/img/icons/wiki.svg"
           :title "Wiki"}]]
   (when next
     [:button.circular-button.check {:onclick (str "validate('" next "')")}
      [:img {:src   "/img/icons/checkmark.svg"
             :title "Fertig"}]])])

(defn nav-bar [lang project-id]
  (println project-id)
  [:nav
   [:a.button {:href (tr/url lang "/projekte")} "← " "Zurück"]
   [:form {:action (tr/url lang "/projekt/speichern")
           :id     "save-form"
           :method "post"}
    [:input {:id    "__anti-forgery-token"
             :name  "__anti-forgery-token"
             :type  :hidden
             :value *anti-forgery-token*}]
    [:input {:name  "html"
             :id    "html-input"
             :type  :hidden
             :value ""}]
    [:input {:name  "css"
             :id    "css-input"
             :type  :hidden
             :value ""}]
    [:input {:name  "id"
             :id    "id"
             :type  :hidden
             :value (str project-id)}]
    [:button
     {:onclick "save()"}
     [:img {:src "/img/icons/save-outline.svg"}] "Speichern"]]])

(defn editor-page [lang code project-id]
  [:main#editor-screen
   [:aside#editor-sidebar
    (nav-bar lang project-id)
    (editor-tabs)
    [:div#editor-wrapper
     [:div#editor.w-full.h-full.language-html {:id "editor"}
      (:html code)]]
    (hidden-code code)]
   [:div#editor-output
    [:iframe#output]]

   ; add js editor scripts
   [:script {:src     "/js/src-min-noconflict/ace.js"
             :type    "text/javascript"
             :charset "utf-9"}]
   [:script {:src "/js/editor.js?v=1"}]])

(defn hidden-block-code [html css result wrong-blocks]
  [:div#hidden-code
   [:#html-base html]
   [:#css-base css]
   [:#result result]
   [:#wrong wrong-blocks]])

(defn block-editor [lang {:mission/keys [world step story-before
                                         story-after content] :as mission}]
  (let [{:mission.content/keys [hidden-html hidden-css
                                result mode wrong-blocks] :as mission-content}
        (first
          (filter
            #(= (:mission.content/difficulty %) :easy)
            content))]
    [:main#block-editor
     [:div#mission-editor
      [:aside#editor-sidebar
       [:nav
        [:a.button {:href (tr/url lang "/world/map/" (name world))} "Zurück"]
        [:h3 (:mission/name mission)]
        [:select {:on-change "change_difficulty(event)"}
         [:option {:value    :easy
                   :selected true} "Einfach"]
         [:option {:value :medium} "Mittel"]
         [:option {:value :hard} "Schwer"]
         ]]
       [:div#editor
        [:div
         [:div#target-hint "Ziehe Blöcke hier hin"]
         [:div#target
          ]]
        [:div
         [:div#selection]]
        [:div
         [:p [:em "Hinweis"]]
         [:#explanation]]]
       [:div
        [:button#evaluate-btn {:on-click "evaluate_code()"} "Prüfen"]]
       #_(hidden-block-code hidden-html hidden-css result wrong-blocks)]
      [:div#editor-output
       [:iframe#output]]

      [:div#tsparticles]

      ]
     [:#chat
      [:#phone
       [:#status-bar
        [:#time (.format (LocalTime/now) (DateTimeFormatter/ofPattern "HH:mm"))]]
       [:#chat-window
        [:#chat-messages ""]
        [:button#chat-next-btn {:on-click "next_message()"} "Weiter"]]

       [:#phone-on-btn]
       [:#phone-volup-btn]
       [:#phone-voldown-btn]]
      [:#close-phone]
      ]
     [:div#overlay.hidden
      [:div
       [:h2 "Gut gemacht!"]
       [:button {:on-click "progress()"} "Weiter"]]]

     ; add js editor scripts
     [:script {:src     "/js/Sortable.js"
               :type    "text/javascript"
               :charset "utf-9"}]
     [:script {:src  "/js/tsparticles.bundle.min.js"
               :type "text/javascript"}]
     [:script {:src  "/js/marked.min.js"
               :type "text/javascript"}]
     [:script {:src "/js/block-editor.js?v=2"}]])
  )