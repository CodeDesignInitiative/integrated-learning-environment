(ns cd.ile.views.chat-screen)

(defn chat-screen []
  (let [conversations cd.ile.core.mock-data/conversations]
    [:div.bg-gradient-to-b.min-h-screen.text-white
     [:h1.text-xl.p-3 "Chats"]
     [:div.flex.flex-row.gap-2
      [:div.border-r.border-slate-300.border-solid

       (map (fn [c]
              [:div.p-3.border-t.border-slate-300.border-solid
               (get-in c [:conversation/with :person/name])
               ])
            conversations)]
      [:div
       (map
         (fn [prev-conv]
           [:div.mb-6
            (map (fn [{:story/keys [person message]}]
                   [:div.p-2.rounded-xl.bg-green-400.mb-2
                    [:h3.text-sm (:person/name person)]
                    [:p.m-0 message]])
                 prev-conv)])
         (:conversation/previous-chats (first conversations)))]]]))