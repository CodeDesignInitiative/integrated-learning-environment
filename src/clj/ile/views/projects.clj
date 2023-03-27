(ns ile.views.projects
  (:require [ile.persistence :as persistence]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))


(defn projects-page [request]
  (let [user-email (get-in request [:session :identity])
        user-projects (persistence/get-user-projects (name user-email))
        templates []]
    [:div
     [:h1 "Freier Code Editor"]

     [:h2 "Meine Projekte"]
     [:div.row
      (map
        (fn [project]
          [:a.button {:href (str "/projekt?id=" (:xt/id project))}
           (or (:user.project/name project) "Unbenannt")])
        user-projects)]


     [:form.column {:action "/projekte/neu"
                    :method "post"}
      [:input {:id    "__anti-forgery-token"
               :name  "__anti-forgery-token"
               :type  :hidden
               :value *anti-forgery-token*}]
      [:label {:for "project-name"}
       "Projekt Name"]
      [:input {:required true
               :name     "project-name"}]
      [:button {:type :submit} "Neues leeres Projekt"]
      ]


     [:h2 "Projekt Vorlagen"]]
    ))