(ns cd.ile.ui.components)

(defn app-button
  "Button Component for the app-like homescreen nav"
  [{:keys [label href bg-color disabled?]}]
  [:div.flex.flex-col.gap-2
   [:a.p-2.rounded-xl.hover:shadow-lg.transition-all.aspect-square
    {:href     href
     :class    bg-color
     :disabled (or disabled? false)}
    ]
   [:span.mx-auto label]
   ])

(defn notes-widget [notes]
  "The widget used to display the current notes & tasks"
  [:div.bg-yellow-300.shadow-lg.text-black
   [:h2 "Notizen"]
   [:ul
    (map
      (fn [n] [:li n])
      notes)]])