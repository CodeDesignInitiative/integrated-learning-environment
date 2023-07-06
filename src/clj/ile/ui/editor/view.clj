(ns ile.ui.editor.view
  (:require [ile.ui.components :as components]
            [ile.dictonary.translations :as tr]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))

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
    (:code/base html)]
   [:#html-snippet
    (:code/snippet html)]
   [:#html-answer
    (:code/answer html)]
   [:#css-base
    (:code/base css)]])

(defn interaction-buttons [next]
  [:div#editor-interaction
   [:button.circular-button.neutral
    {:onclick "show_help()"}
    [:img {:src "/img/icons/wiki.svg"
           :title "Wiki"}]]
   (when next
     [:button.circular-button.check {:onclick (str "validate('" next "')")}
      [:img {:src   "/img/icons/checkmark.svg"
             :title "Fertig"}]])])

(defn nav-bar [lang next]
  [:nav
   [:a.button {:href (tr/url lang "/projekte")} "Zurück"]
   (when-not next
     [:form {:action "/projekt/speichern"
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
               :value ""}]
      [:button
       {:onclick "save()"} "Speichern"]])])

(defn editor-page [lang code next]
  [:main#editor-screen
   [:aside#editor-sidebar
    (nav-bar lang next)
    (editor-tabs)
    [:div#editor-wrapper
     [:div#editor.w-full.h-full.language-html {:id "editor"}
      (:code/line (:html code))]
     #_(interaction-buttons next)]
    (hidden-code code)]
   [:div#editor-output
    [:iframe#output]]

   ; add js editor scripts
   [:script {:src "/js/src-min-noconflict/ace.js"
             :type "text/javascript"
             :charset "utf-9"}]
   [:script {:src "/js/editor.js"}]])