(ns ile.ui.admin.core
  (:require
    [ile.templates.core :as templates]
    [ile.ui.admin.view :as view]
    [ile.util :as util]))

(defn users-page [request]
  (view/users-page))

(defn templates-page [request]
  (let [templates (templates/find-all-templates)]
    (view/templates-page templates)))

(defn template-edit-page [request]
  (let [template-id (util/get-path-param request :id)
        template (templates/find-template template-id)]
    (view/template-edit-page template)))

(defn map-template-form
  [{:strs [template_name visible? html_code css_code js_code]}]
  #:template{:name     template_name
             :visible? (= "checked" visible?)
             :code     {:html html_code
                        :css  css_code
                        :js   js_code}})

(defn template-post-page [request]
  (let [template-id (util/get-path-param request :id)
        template (:form-params request)
        template' (map-template-form template)]
    (if-not (nil? template-id)
      (templates/update-template!
        (merge {:xt/id template-id} template'))
      (templates/create-template! template'))
    (template-edit-page request)))

(defn story-editor-page [request]
  (view/story-page))

(defn admin-page [request]
  (view/admin-page))

(def routes
  ["/admin"
   ["" {:get admin-page}]
   ["/users" {:get users-page}]
   ["/templates" {:get templates-page}]
   ["/template/:id" {:get  template-edit-page
                     :post template-post-page}]
   ["/story" {:get story-editor-page}]])