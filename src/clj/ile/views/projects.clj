(ns ile.views.projects
  (:require [ile.persistence :as persistence]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))


(defn projects-page [request]
  (let [user-email (get-in request [:session :identity])
        user-projects (persistence/get-user-projects (name user-email))
        templates []]
    [:main#projects-page
     [:span
      [:a.button {:href "/"} "Zurück"]]
     [:h1 "Freier Code Editor"]

     [:h2 "Meine Projekte"]
     [:div.row.project-list
      (map
        (fn [project]
          [:a.button.project {:href (str "/projekt?id=" (:xt/id project))}
           (or (:user.project/name project) "Unbenannt")])
        user-projects)]


     [:form#new-project.column {:action "/projekte/neu"
                                :method "post"}
      [:h4 "Neues Projekt"]
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

     [:hr]

     [:h2 "Projekt Vorlagen"]

     [:div.row.gap
      [:form.project-template.column {:action "/projekte/neu"
                                      :method "post"}
       [:h4 "Blog Vorlage"]
       [:input {:id    "__anti-forgery-token"
                :name  "__anti-forgery-token"
                :type  :hidden
                :value *anti-forgery-token*}]
       [:input {:name  "template"
                :type  :hidden
                :value "blog"}]
       [:label {:for "project-name"}
        "Projekt Name"]
       [:input {:required true
                :name     "project-name"}]
       [:button {:type :submit} "Neues Blog-Projekt"]
       ]
      [:form.project-template.column {:action "/projekte/neu"
                                      :method "post"}
       [:h4 "Produkte Vorlage"]
       [:input {:id    "__anti-forgery-token"
                :name  "__anti-forgery-token"
                :type  :hidden
                :value *anti-forgery-token*}]
       [:input {:name  "template"
                :type  :hidden
                :value "produkte"}]
       [:label {:for "project-name"}
        "Projekt Name"]
       [:input {:required true
                :name     "project-name"}]
       [:button {:type :submit} "Neues Produkte-Projekt"]
       ]]]
    ))