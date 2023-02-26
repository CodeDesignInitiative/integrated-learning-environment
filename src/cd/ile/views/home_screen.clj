(ns cd.ile.views.home-screen
  (:require [cd.ile.ui.components :as components]
            [cd.ile.core.date-util :as date-util]
            [cd.ile.core.mock-data :as mock]))

(defn- notifications-widget [notifications]
  [:div
   [:h2 "Benachrichtigungen"]
   [:ul
    (map
      (fn [{:keys [date heading msg]}]
        [:li
         (date-util/format-date date)
         [:h4 heading]
         [:p msg]])
      notifications)]])

(defn home-screen []
  [:main.bg-gradient-to-b.text-white.p-7
   {:class "from-[#372748] to-[#131424]"}
   [:div.grid.gap-4
    {:class "grid-cols-[380px,1fr]"}
    [:.grid.gap-4
     {:class "grid-rows-[1fr,320px]"}
     [:.grid.gap-16.grid-cols-2.grid-rows-2
      (components/app-button {:label    "Chat"
                              :href     "/app/chat"
                              :icon     "chat"
                              :bg-color "bg-green-400"})
      (components/app-button {:label    "Aufträge"
                              :href     "/app/auftraege"
                              :icon     "jobs"
                              :bg-color "bg-orange-600"})
      (components/app-button {:label    "Wiki"
                              :href     "/app/wiki"
                              :icon     "wiki"
                              :bg-color "bg-fuchsia-400"})
      (components/app-button {:label    "Code Editor"
                              :href     "/app/editor"
                              :icon     "code"
                              :bg-color "bg-sky-500"})
      ]

     (components/notes-widget mock/notes)]
    (notifications-widget mock/notifications)]])