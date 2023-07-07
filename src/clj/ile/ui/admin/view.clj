(ns ile.ui.admin.view
  (:require [ile.util :as util]))


(defn users-page [])

(defn- template-row-item
  [{:template/keys [name code visible name-translations]
    :xt/keys       [id]}]
  [:a.button {:href (str "/admin/template/" id)}
   name])

(defn templates-page [templates]
  [:main
   [:h1 "All templates"]
   (map template-row-item templates)])

(defn template-edit-page
  [{:template/keys [name code visible? name-translations] :as template
    :xt/keys       [id]}]
  [:<>
   [:nav
    [:a.button {:href "/admin/templates"} "Zurück / Abrechen"]]
   [:main#template-editor
    [:h1 (str "Template \"" name "\" bearbeiten")]
    [:form.form-tile
     {:action (str "/admin/template/" id)
      :method :post}
     (util/hidden-anti-forgery-field)
     [:label "Name"]
     [:input {:value name
              :name  "template_name"}]
     [:label "Sichtbar?"]
     [:input {:type    :checkbox
              :checked (or visible? false)
              :name    "visible?"}]
     [:label "HTML Code"]
     [:textarea {:value (:html code)
                 :name  "html_code"}]
     [:label "CSS Code"]
     [:textarea {:value (:css code)
                 :name  "css_code"}]
     [:label "JS Code"]
     [:textarea {:value (:js code)
                 :name  "js_code"}]
     [:button {:type :submit} "Speichern"]]]])

(defn story-page [])

(defn admin-page []

  [:main
   [:a.button {:href "/admin/users"} "Users"]
   [:a.button {:href "/admin/templates"} "Templates"]
   [:a.button {:href "/admin/story"} "Story"]])