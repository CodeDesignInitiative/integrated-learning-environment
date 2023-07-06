(ns ile.ui.projects.core
  (:require
    [ile.dictonary.translations :as tr]
    [ile.ui.editor.core :as editor]
    [ile.persistence :as persistence]
    [ile.ui.projects.view :as view]
    [ile.util :as util]
    [ring.util.response :as response]))

(defn- projects-page [request]
  (let [lang (tr/lang request)
        user-email (get-in request [:session :identity])
        user-projects (if user-email (or (persistence/get-user-projects (name user-email)) []) [])]
    (view/projects-page lang user-projects)))

(defn- projects-page-new-project [request]
  #_(let [template (get-in request [:form-params "template"])
          project-name (get-in request [:form-params "project-name"])
          user-email (-> (get-in request [:session :identity]) name)
          template-code (get-template-code template)
          project-id (random-uuid)]
      (do
        (persistence/create-user-project (merge {:xt/id project-id}
                                                #:user.project{:name  project-name
                                                               :owner user-email
                                                               :css   (if template (:css template-code) "")
                                                               :html  (if template (:html template-code) "")}))
        (response/redirect (str "/projekt?id=" project-id)))))


(defn project-editor [request]
  (let [lang (tr/lang request)
        project-id (get-in request [:query-params "id"])
        project (persistence/find-user-project (parse-uuid project-id))]
    (editor/editor
      lang
      {:html {:code/line (:user.project/html project)}
       :css  {:code/base (:user.project/css project)}}
      nil)))


(defn save-project [request]
  (let [project-id (parse-uuid (get-in request [:form-params "id"]))
        html (get-in request [:form-params "html"])
        css (get-in request [:form-params "css"])
        project (persistence/find-user-project project-id)]
    (persistence/save-project (merge project
                                     {:user.project/html html
                                      :user.project/css  css}))
    (response/redirect (str "/projekt?id=" project-id))))

(def routes
  ["/:lang"
   ["/projekt"
    ["" {:get project-editor}]
    ["/speichern" {:post save-project}]]
   ["/projekte" {:get  projects-page
                 :post projects-page-new-project}]])