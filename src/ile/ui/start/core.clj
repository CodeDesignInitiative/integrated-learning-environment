(ns ile.ui.start.core
  (:require
    [ile.dictonary.translations :as tr]
    [ile.ui.start.view :as view]))

(defn- start-screen [request]
  (let [lang (tr/lang request)
        logged-in? (some? (get-in request [:session :user]))
        is-admin? (boolean (some #{:admin} (get-in request [:session :user :user/roles])))
        is-teacher? (boolean (some #{:teacher} (get-in request [:session :user :user/roles])))]
    (view/start-screen lang logged-in? is-admin? is-teacher?)))

(defn- info-screen [request]
  [:<>
   [:nav
    [:a.button {:href "/"}
     "Zurück"]]
   [:main.m3
    [:h1 "Info"]

    [:ul
     [:li
      [:a.link {:href "https://code.design/impressum"}
       "Impressum"]]
     [:li
      [:a.link {:href "/datenschutz"}
       "Datenschutz"]]]]]
  )

(def routes
  [""
   ["/:lang/" {:get start-screen}]
   ["/info" {:get info-screen}]])
