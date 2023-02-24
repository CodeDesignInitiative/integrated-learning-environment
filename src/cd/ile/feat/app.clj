(ns cd.ile.feat.app
  (:require [com.biffweb :as biff :refer [q]]
            [cd.ile.middleware :as mid]
            [cd.ile.ui :as ui]
            [rum.core :as rum]
            [xtdb.api :as xt]
            [ring.adapter.jetty9 :as jetty]
            [cheshire.core :as cheshire]))

(defn app [{:keys [session biff/db] :as req}]
  (let [{:user/keys [email]} (xt/entity db (:uid session))]
    (ui/page
      {}
      nil
      [:div "Signed in as " email ". "
       (biff/form
         {:action "/auth/signout"
          :class  "inline"}
         [:button.text-blue-500.hover:text-blue-800 {:type "submit"}
          "Sign out"])
       "."]
      [:.h-6 "ILE"]
      [:div
       [:p [:a.text-blue-500.hover:text-blue-800 {:href "/chat"} "Chat"]]
       [:p [:a.text-blue-500.hover:text-blue-800 {:href "/auftraege"} "Aufträge"]]
       [:p [:a.text-blue-500.hover:text-blue-800 {:href "/wiki"} "Wiki"]]
       [:p [:a.text-blue-500.hover:text-blue-800 {:href "/editor"} "Code Editor"]]
       ]
      [:div
       [:h2 "Notizen"]
       [:ul
        (map
          (fn [n] [:li n])
          ["Chat öffnen"
           "Code Editor reinschauen"
           "Aufträge checken"])]]
      [:div
       [:h2 "Benachrichtigungen"]
       [:ul
        (map
          (fn [{:keys [date heading msg]}]
            [:li
             date
             [:h4 heading]
             [:p msg]])
          [{:date    "21. Februar 2023"
            :heading "Willkommen im Team!"
            :msg     "Damit du hier einen guten Start hast, kümmert sich Xara um dich. Sie sollte dir schon geschrieben haben. Schau mal in den Chat.\nIch habe es dir auch als Erinnerung auf deinen digitalen Notizzettel geschrieben."}
           {:date    "21. Februar 2023"
            :heading "Du hast einen neuen Auftrag!"
            :msg     "Schau in deine Aufträge links und sieh nach was es so gibt."}
           {:date    "21. Februar 2023"
            :heading "Danke für deine Hilfe bei dem letzten Projekt."
            :msg     "..."}])]])))


(def features
  {:routes ["/app" {:middleware [mid/wrap-signed-in]}
            ["" {:get app}]]})
