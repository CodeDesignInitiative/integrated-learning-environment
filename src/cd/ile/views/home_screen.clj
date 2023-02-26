(ns cd.ile.views.home-screen
  (:require [cd.ile.ui.components :as components]
            [cd.ile.core.date-util :as date-util]
            [cd.ile.core.mock-data :as mock]))

(defn- notifications-widget [notifications]
  [:div.overflow-y-auto
   [:h2 "Benachrichtigungen"]
   [:ul.list-none.pl-0
    (map
      (fn [{:keys [date heading msg]}]
        [:li.px-3.py-2.mb-8.rounded-md
         {:class "bg-zinc-500/30"}
         [:span.text-sm.text-zinc-400
          (date-util/format-date date)]
         [:h4.text-lg heading]
         [:p msg]])
      notifications)]])

(defn home-screen []
  [:main.bg-gradient-to-b.text-white.p-7.flex.flex-row.gap-8
   {:class "from-[#372748] to-[#131424]"}
   [:aside.flex.flex-col.place-content-between
    [:.app-grid
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
                             :bg-color "bg-sky-500"})]

    (components/notes-widget mock/notes)]
   (notifications-widget mock/notifications)])