(ns ile.ui.components)

(defn app-button
  "Button Component for the app-like home-screen nav"
  [{:keys [label href id disabled? icon notifications]}]
  [:div#app-button
   (when notifications
     [:span#app-notification
      [:span notifications]])
   [:a
    {:href     href
     :disabled (or disabled? false)}
    [:div.app-button
     {:id id}
     [:img {:src (str "/img/icons/" (or icon "missing") ".svg")}]]

    [:label label]]])

(defn notes-widget [notes]
  "The widget used to display the current notes & tasks"
  [:div#notes
   [:h2 "Notizen"]
   [:ul
    (map
      (fn [n] [:li n])
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