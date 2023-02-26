(ns cd.ile.views.chat-screen
  (:require [cd.ile.ui.components :as components]))

(defn chat-screen []
  (let [conversations cd.ile.core.mock-data/conversations]
    [:div.bg-gradient-to-b.min-h-screen.text-white
     [:h1.text-xl.p-3.text-center.border-b.border-zinc-500.border-solid.bg-zinc-800
      "Chats"]
     [:div.flex.flex-row
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
                   "/img/persons/test.jpg")}]
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