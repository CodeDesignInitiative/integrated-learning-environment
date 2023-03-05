(ns cd.ile.views.editor-screen

  (:require [cd.ile.ui.components :as components]
            [cd.ile.core.mock-data :as mock]))

(defn- browser-chrome [body]
  "Render an empty browser chrome"
  [:figure.ml-auto.relative.max-w-full.h-auto.rounded-b-lg
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
  [:ul.flex.flex-row.gap-4.items-center.list-none
   (map
     (fn [n] [:li.px-4.py-2.bg-purple-300.bg-opacity-10.rounded-lg
              (when (= active n) {:class "bg-[#FBB328] text-black bg-opacity-100"})
              n])
     tabs)])


(defn editor-screen [html css]
  [:main.grid.gap-4.text-white.bg-gradient-to-b
   {:class "grid-cols-[380px,1fr] from-[#372748] to-[#131424]"}
   [:aside.grid.gap-4.bg-opacity-60
    {:class "grid-rows-[1fr,320px] bg-[#212121]"}
    [:div.p-4
     (editor-tabs ["HTML" "CSS"] "HTML")
     [:<>
      [:div.w-full.h-full.rounded-lg.text-black.language-html {:id "editor"}
       html]
      [:#css-base
       css]]]
    (components/notes-widget mock/notes)
    ]
   [:div.p-4
    [:div.bg-purple-400.rounded-lg.w-full.bg-opacity-5.h-full
     [:span.my-auto.mx-auto.h-full
      (browser-chrome [:span "hello!!!"])]]]])