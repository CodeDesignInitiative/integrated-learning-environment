(ns ile.views.projects
  (:require [ile.persistence :as persistence]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))


(defn template-selection []
  [:<>
   [:form.new-project.column {:action "/projekte/neu"
                              :method "post"}
    [:h4 "Steckbrief 2: Farbe"]
    [:input {:id    "__anti-forgery-token"
             :name  "__anti-forgery-token"
             :type  :hidden
             :value *anti-forgery-token*}]
    [:input {:name  "template"
             :type  :hidden
             :value "list-color-de"}]
    [:label {:for "project-name"}
     "Projekt Name"]
    [:input {:required    true
             :placeholder "Projekt Name"
             :name        "project-name"}]
    [:button {:type :submit} "Neuer Steckbrief"]]
   [:form.new-project.column {:action "/projekte/neu"
                              :method "post"}
    [:h4 "Характеристики 2: цвет"]
    [:input {:id    "__anti-forgery-token"
             :name  "__anti-forgery-token"
             :type  :hidden
             :value *anti-forgery-token*}]
    [:input {:name  "template"
             :type  :hidden
             :value "list-color-ru"}]
    [:label {:for "project-name"}
     "Projekt Name"]
    [:input {:required    true
             :placeholder "Projekt Name"
             :name        "project-name"}]
    [:button {:type :submit} "Новый профиль"]]
   [:form.new-project.column {:action "/projekte/neu"
                              :method "post"}
    [:h4 "Steckbrief"]
    [:input {:id    "__anti-forgery-token"
             :name  "__anti-forgery-token"
             :type  :hidden
             :value *anti-forgery-token*}]
    [:input {:name  "template"
             :type  :hidden
             :value "list-de"}]
    [:label {:for "project-name"}
     "Projekt Name"]
    [:input {:required    true
             :placeholder "Projekt Name"
             :name        "project-name"}]
    [:button {:type :submit} "Neuer Steckbrief"]]
   [:form.new-project.column {:action "/projekte/neu"
                              :method "post"}
    [:h4 "Характеристики"]
    [:input {:id    "__anti-forgery-token"
             :name  "__anti-forgery-token"
             :type  :hidden
             :value *anti-forgery-token*}]
    [:input {:name  "template"
             :type  :hidden
             :value "list-ru"}]
    [:label {:for "project-name"}
     "Projekt Name"]
    [:input {:required    true
             :placeholder "Projekt Name"
             :name        "project-name"}]
    [:button {:type :submit} "Новый профиль"]]
   [:form.new-project.column {:action "/projekte/neu"
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
    [:input {:required    true
             :placeholder "Projekt Name"
             :name        "project-name"}]
    [:button {:type :submit} "Neues Blog-Projekt"]]

   [:form.new-project.column {:action "/projekte/neu"
                                   :method "post"}
    [:h4 "Raster Vorlage"]
    [:input {:id    "__anti-forgery-token"
             :name  "__anti-forgery-token"
             :type  :hidden
             :value *anti-forgery-token*}]
    [:input {:name  "template"
             :type  :hidden
             :value "grid"}]
    [:label {:for "project-name"}
     "Projekt Name"]
    [:input {:required    true
             :placeholder "Projekt Name"
             :name        "project-name"}]
    [:button {:type :submit} "Neues Produkte-Projekt"]]
   ])

(defn my-projects-list [user-projects]
  [:div.column.project-list
   (map
     (fn [project]
       [:a.button.project {:href (str "/projekt?id=" (:xt/id project))}
        (or (:user.project/name project) "Unbenannt")
        [:img {:src "img/icons/chevron-forward.svg"}]])
     user-projects)])

(defn new-empty-project-form []
  [:form.new-project.column {:action "/projekte/neu"
                             :method "post"}
   [:h4 "Leeres Projekt"]
   [:input {:id    "__anti-forgery-token"
            :name  "__anti-forgery-token"
            :type  :hidden
            :value *anti-forgery-token*}]
   [:label {:for "project-name"}
    "Projekt Name"]
   [:input {:required    true
            :placeholder "Projekt Name"
            :name        "project-name"}]
   [:button {:type :submit} "Neues leeres Projekt"]])

(defn projects-page [request]
  (let [user-email (get-in request [:session :identity])
        user-projects (persistence/get-user-projects (name user-email))
        templates []]
    [:main#projects-page
     [:nav
      [:a.button {:href "/"} "Zurück"]
      [:h1 "Freier Code Editor"]]

     [:div.row.p-2.gap-2
      [:div.column.flex-1
       [:h2 "Meine Projekte"]
       (my-projects-list user-projects)]

      [:div
       [:h2 "Neues Projekt"]
       [:div.column.gap
        (new-empty-project-form)
        (template-selection)]]]]))