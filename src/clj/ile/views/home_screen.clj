(ns ile.views.home-screen
  (:require [ile.ui.components :as components]
            [ile.core.date-util :as date-util]
            [ile.core.mock-data :as mock]))

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

(defn- signout-button []
  [:a.button {:href "/logout"}
   "Ausloggen"
   [:img {:src "/img/icons/logout.svg"}]])

(defn- settings-button []
  [:a.p-4.text-black.rounded-full.font-bold.flex.flex-row.gap-2.items-center
   {:class "bg-[#6FBFD8]"
    :href  "/settings"}
   "Einstellungen"
   [:svg.w-6.h-6 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke-width "1.5" :stroke "currentColor"}
    [:path {:stroke-linecap "round" :stroke-linejoin "round" :d "M4.5 12a7.5 7.5 0 0015 0m-15 0a7.5 7.5 0 1115 0m-15 0H3m16.5 0H21m-1.5 0H12m-8.457 3.077l1.41-.513m14.095-5.13l1.41-.513M5.106 17.785l1.15-.964m11.49-9.642l1.149-.964M7.501 19.795l.75-1.3m7.5-12.99l.75-1.3m-6.063 16.658l.26-1.477m2.605-14.772l.26-1.477m0 17.726l-.26-1.477M10.698 4.614l-.26-1.477M16.5 19.794l-.75-1.299M7.5 4.205L12 12m6.894 5.785l-1.149-.964M6.256 7.178l-1.15-.964m15.352 8.864l-1.41-.513M4.954 9.435l-1.41-.514M12.002 12l-3.75 6.495"}]]])
(defn- company-logo []
  [:h2.font-bold
   "The Good" [:br] "Company Inc."])

(defn home-screen []
  [:main#home-screen
   [:aside
    [:.app-grid
     (components/app-button {:label         "Chat"
                             :href          "/chat"
                             :icon          "chat"
                             :id            "chat-icon"
                             :notifications 1})
     (components/app-button {:label     "Aufträge"
                             :href      "/auftraege"
                             :icon      "jobs"
                             :id        "jobs"
                             :disabled? true})
     (components/app-button {:label     "Wiki"
                             :href      "/wiki"
                             :icon      "wiki"
                             :id        "wiki"
                             :disabled? true})
     (components/app-button {:label     "Code Editor"
                             :href      "/projekte"
                             :icon      "code"
                             :id        "code"
                             :disabled? true})]

    (components/notes-widget mock/notes)]
   [:div#home-feed.flex.flex-col.items-end.gap-4.space-between
    (notifications-widget mock/notifications)
    [:#status-bar.row.align-center
     (company-logo)
     ;(settings-button)
     (signout-button)]]])