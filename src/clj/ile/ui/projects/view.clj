(ns ile.ui.projects.view
  (:require
    [ile.dictonary.translations :as tr]
    [ile.util :as util]
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

(defn project-list-entry [project lang]
  [:a.button.project {:href (tr/url lang "/projekt/editor/" (:xt/id project))}
   [:h3
    (or (:user.project/name project) "Unbenannt")]])

(defn my-projects-list [user-projects lang]
  [:<> (map #(project-list-entry % lang) user-projects)])

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

(def new-project-btn
  [:a.button.new-project {:href "/projekt/neu"}
   [:h1 "+"]
   [:h3 "Neues Projekt"]])

(defn template-option [{:template/keys [name]
                        :xt/keys [id]}]
  [:div.tile
   [:label {:for id} name]
   [:input {:type :radio
            :id id
            :name "template"
            :value id}]])

(defn new-project-page [lang templates]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/projekte")} "Abbrechen"]
    [:h2 "Neues Projekt erstellen"]
    [:div]]
   [:main#new-project-page
    [:form#new-project-form
     {:action (tr/url lang "/projekt/neu")
      :method :post}
     (util/hidden-anti-forgery-field)
     [:.form-tile
      [:label "Projekt Name"]
      [:input {:placeholder "Mein Projekt"
               :name "project_name"}]

      [:button {:type :submit}
       "Erstellen"]]
     [:div
      [:div.tile#empty-project-option
       [:label {:for :empty} "– Ohne Vorlage –"]
       [:input {:type :radio
                :id :empty
                :name "template"
                :checked true
                :value "__empty__"}]]
      (map template-option templates)]]]])

(defn projects-page
  "Page containing projects that a specific user created.
  Can direct to create-project page."
  [lang projects]
  [:<>
   [:nav
    [:a.button {:href (tr/url lang "/")} "← " "Zurück"]
    [:h1 "Editor"]
    [:a.button {:href (tr/url lang "/projekt/neu")}
     "Neues Projekt"
     [:img {:src "/img/icons/rocket.svg"}]]]
   [:main#projects-page
    [:h2 "Meine Projekte"]
    [:.project-list
     #_new-project-btn
     (my-projects-list projects lang)]]])