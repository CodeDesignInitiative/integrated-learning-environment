(ns cd.ile.views.chat-screen
  (:require [cd.ile.courses.html-website :as html-website]
            [cd.ile.ui.components :as components]
            [cd.ile.core.mock-data :as mock]))

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
     [:p.m-2 "Sende deine Antwort..."]
     [:div.chat-next-btn
      [:a {:href (str "/app/auftrag?job=" job "&step=" (+ job-step 1))}
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
                 [:a {:href (str "/app/auftrag?job=" job "&step=" job-step "&chat=" (+ chat-step 1))}
                  [:p.m-0 answer]]]
                )
              answer-choices)]]

          ; when no anser choices either "next" button
          ; or nothing, when end of conversation
          (when (not (= (count conversation) chat-step))
            [:div.p-2.rounded-xl.bg-orange-400.mb-2
             [:a {:href (str "/app/auftrag?job=" job "&step=" job-step "&chat=" (+ chat-step 1))}
              [:p.m-0 "Weiter"]]]))])

     (next-button conversation chat-step job job-step)]
    ]])


(defn chat-screen []
  (let [conversations mock/conversations]
    [:main.bg-gradient-to-b.text-white
     (chat-interactive-screen html-website/start-chat-with-edna 1 "website1" 1)
     #_[:div.flex.flex-row
        [:div.bg-zinc-700.w-48

         (map (fn [c]
                [:div.p-3.border-b.border-zinc-600.border-solid
                 (get-in c [:conversation/with :person/name])
                 [:p.m-0.text-sm
                  (get-in c [:conversation/with :person/organization])]])
              conversations)]
        [:div.grow.bg-slate-700
         [:div.flex.flex-row.bg-zinc-600
          [:img.m-2.aspect-square.object-cover.w-12.h-12.rounded-full
           {:src (or (get-in (first conversations) [:conversation/with :person/img])
                     "/img/persons/avatar_2.png")}]
          [:div.m-2
           [:div (get-in (first conversations) [:conversation/with :person/name])]
           [:div.text-sm (get-in (first conversations) [:conversation/with :person/organization])]]
          ]
         [:div.overflow-y-auto.chat-height
          [:p.mt-2.p-2.text-center.text-sm.bg-slate-500 "Neue Nachrichten"]
          (map
            (fn [prev-conv]
              [:div.m-3.max-w-2xl.mx-auto.px-3
               (map (fn [{:story/keys [person message video]}]
                      [:div.p-2.rounded-xl.bg-green-400.mb-2
                       [:h3.text-sm (:person/name person)]
                       [:p.m-0 message]])
                    prev-conv)])
            (:conversation/open-chats (first conversations)))

          [:p.mt-2.p-2.text-center.text-sm.bg-slate-800 "Vorherige Chats"]
          (components/conversation-widget (:conversation/previous-chats (first conversations)))]]]]))