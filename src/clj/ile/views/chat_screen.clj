(ns ile.views.chat-screen
  (:require [ile.courses.html-website :as html-website]
            [ile.ui.components :as components]
            [ile.core.mock-data :as mock]))

(defn profile
  "Displays the person the user is chatting with."
  [person]
  [:div.flex.flex-col.gap-2.bg-zinc-600
   [:img.profile-chat.m-2
    {:src (or (:person/picture person)
              "/img/persons/avatar_2.png")}]
   [:div.m-2.text-center
    [:div.text-xl (:person/name person)]
    [:div.text-sm (get-in person [:conversation/with :person/organization])]]])

(defn next-button
  [conversation chat-step job job-step]
  (when (= (count conversation) chat-step)
    [:div#chat-keyboard
     [:p.m-2 "Los gehts..."]
     [:div.chat-next-btn
      [:a {:href (str "/auftrag?job=" job "&step=" (+ job-step 1))}
       [:p.m-0 "Weiter"]]]]))


(defn chat-interactive-screen [conversation chat-step job job-step]
  [:main.bg-gradient-to-b.text-white
   [:div.grow.bg-slate-700.full-height.flex.flex-row

    ; profile header
    (profile (:story/person (get conversation 0)))

    ; messages

    [:div#chat.flex-grow

     [:div.chat-spacer]

     (when (> (count conversation) 1)

       (map
         (fn [{:story/keys [person message video answer-choices]}]
           [:<>
            [:div.chat-bubble
             [:h3.text-sm (:person/name person)]
             [:p.m-0 message]]
            (when answer-choices
              [:div.chat-user-answers
               [:div.chat-bubble.user
                [:p.m-0 (first answer-choices)]]])])
         (take (- chat-step 1) conversation)))

     ; interactions

     (let [{:story/keys [person message video answer-choices]} (get conversation (- chat-step 1))]
       [:<>

        ; chat message

        [:div.chat-bubble
         [:h3 (:person/name person)]
         [:p.m-0 message]]

        ; answer choices, if present

        (if answer-choices
          [:div#chat-keyboard
           [:p.m-2 "Sende deine Antwort..."]
           ; when end of conversation: next button to next step

           [:div.chat-user-answers
            (map
              (fn [answer]
                [:div.chat-bubble.user
                 [:a {:href (str "/auftrag?job=" job "&step=" job-step "&chat=" (+ chat-step 1))}
                  [:p.m-0 answer]]])
              answer-choices)]]

          ; when no anser choices either "next" button
          ; or nothing, when end of conversation
          (when (not (= (count conversation) chat-step))
            [:div#chat-keyboard
             [:div.chat-next-btn
              [:a {:href (str "/auftrag?job=" job "&step=" job-step "&chat=" (+ chat-step 1))}
               [:p.m-0 "Weiter"]]]]))])

     (next-button conversation chat-step job job-step)]
    ]])


(defn chat-screen []
  [:main.bg-gradient-to-b.text-white
   (chat-interactive-screen html-website/start-chat-with-edna 1 "website1" 1)])