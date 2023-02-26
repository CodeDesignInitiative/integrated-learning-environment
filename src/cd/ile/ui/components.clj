(ns cd.ile.ui.components)

(defn app-button
  "Button Component for the app-like home-screen nav"
  [{:keys [label href bg-color disabled? icon]}]
  [:div.flex.flex-col.gap-2
   [:a
    {:href     href
     :disabled (or disabled? false)}
    [:div.p-2.rounded-xl.hover:shadow-lg.transition-all.aspect-square
     {:class    bg-color}
     [:img.p-4.invert {:src (str "/img/icons/" (or icon "missing") ".svg")}]]

    [:div.text-center.mt-2 label]]
   ])

(defn notes-widget [notes]
  "The widget used to display the current notes & tasks"
  [:div.bg-yellow-300.shadow-lg.text-black.p-4
   [:h2.text-3xl.font-bold.caveat "Notizen"]
   [:ul.pl-4.caveat.text-3xl.list-none.leading-7
    (map
      (fn [n] [:li.leading-loose n])
      notes)]])