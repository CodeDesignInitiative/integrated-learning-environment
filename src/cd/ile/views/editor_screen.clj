(ns cd.ile.views.editor-screen

  (:require [cd.ile.ui.components :as components]
            [cd.ile.core.mock-data :as mock]))

(defn- editor-output []
  "Render an empty browser chrome"
  [:div#browser
   {:class "shadow-[0_2.75rem_3.5rem_-2rem_rgb(0_0_0_/_20%),_0_0_5rem_-2rem_rgb(0_0_0_/_15%)]"}
   [:div.relative.flex.items-center.bg-gray-700.rounded-t-lg.py-2.px-24
    [:.flex.space-x-1.absolute.left-4.-translate-y-1
     {:class "top-2/4"}
     [:span.w-2.h-2.bg-gray-600.rounded-full]
     [:span.w-2.h-2.bg-gray-600.rounded-full]
     [:span.w-2.h-2.bg-gray-600.rounded-full]]
    [:.flex.justify-center.items-center.w-full.h-full.bg-gray-600.text-gray-400.rounded-sm
     {:class "text-.25rem"}
     "www.code.editor"]]
   [:iframe#output.bg-gray-800.rounded-b-lg.w-full.h-full]
   ])

(defn- editor-tabs [tabs active]
  "The editor tab bar"
  [:div.flex.flex-row.gap-4.items-center.list-none.mb-4
   (map
     (fn [n] [:span.px-4.py-2.bg-purple-300.bg-opacity-10.rounded-lg
              (when (= active n) {:class "active-tab"})
              n])
     tabs)])

(defn hidden-code [{:keys [html css]}]
  [:div
   [:#html-base
    (:code/base html)]
   [:#html-snippet
    (:code/snippet html)]
   [:#css-base
    (:code/base css)]])

(defn interaction-buttons [next]
  [:div#editor-interaction
   [:button.circular-button.neutral
    [:img {:src "/img/icons/wiki.svg"
           :title "Wiki"}]]
   [:a.circular-button.check {:href next}
    [:img {:src "/img/icons/checkmark.svg"
           :title "Fertig"}]]])

(defn editor-screen [code notes next]
  [:main#editor-screen
   [:aside#editor-sidebar
    ; (editor-tabs ["HTML" "CSS"] "HTML")
    [:div#editor-wrapper
     [:div#editor.w-full.h-full.language-html {:id "editor"}
      (:code/line (:html code))]
     (interaction-buttons next)]
    (hidden-code code)
    (components/notes-widget notes)]

   [:div#editor-output
    (editor-output)]

   ; add js editor scripts
   [:script {:src "/js/src-min-noconflict/ace.js"
             :type "text/javascript"
             :charset "utf-9"}]
   [:script {:src "/js/editor.js"}]])