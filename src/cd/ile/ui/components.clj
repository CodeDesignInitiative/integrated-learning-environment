(ns cd.ile.ui.components)

(defn app-button
  "Button Component for the app-like home-screen nav"
  [{:keys [label href bg-color disabled? icon]}]
  [:div.flex.flex-col.gap-2
   [:a
    {:href     href
     :disabled (or disabled? false)}
    [:div.p-2.rounded-3xl.hover:shadow-lg.transition-all.aspect-square
     {:class bg-color}
     [:img.p-4.invert {:src (str "/img/icons/" (or icon "missing") ".svg")}]]

    [:div.text-center.mt-2 label]]])

(defn notes-widget [notes]
  "The widget used to display the current notes & tasks"
  [:div.bg-yellow-300.shadow-lg.text-black.p-4
   [:h2.text-3xl.font-bold.caveat "Notizen"]
   [:ul.pl-4.caveat.text-3xl.list-none.leading-7
    (map
      (fn [n] [:li.leading-loose n])
      notes)]])

(defn video-widget
  "Displays a YouTube video embed, if video URL is not nil."
  [video]
  (when video
    [:iframe.rounded-xl.mb-2
     {:width           "560"
      :height          "315"
      :src             video
      :title           "YouTube video player"
      :frameborder     "0"
      :allow           "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
      :allowfullscreen "true"}]))

(defn conversation-widget
  "Displays the messages of a given conversation without providing any interaction."
  [conversation]
  (map
    (fn [prev-conv]
      [:div.mb-10.m-3.max-w-2xl.mx-auto.px-3
       (map (fn [{:story/keys [person message video answer-choices]}]
              [:<>
               [:div.py-3.px-4.rounded-xl.bg-green-400.mb-2.mr-5
                [:h3.text-sm (:person/name person)]
                (video-widget video)
                [:p.m-0 message]]
               (when answer-choices
                 [:div.py-3.px-4.rounded-xl.bg-sky-400.mb-2.ml-5
                  [:h3.text-sm "Du"]
                  [:p.m-0 (first answer-choices)]])])
            prev-conv)])
    conversation))