(ns ile.ui.projects.core
  (:require
    [ile.dictonary.translations :as tr]
    [ile.templates.core :as templates]
    [ile.ui.editor.core :as editor]
    [ile.ui.projects.view :as view]
    [ile.projects.core :as projects]
    [ile.core.util :as util]
    [ring.util.response :as response]))

(defn- projects-page [request]
  (let [lang (tr/lang request)
        logged-in? (some? (get-in request [:session :user]))
        user-name (get-in request [:session :user :xt/id])
        user-projects (if user-name (or (projects/find-user-projects (name user-name)) []) [])]
    (view/projects-page lang logged-in? user-projects)))

(defn- convert-project [{:user.project/keys [name css html owner]
                         :xt/keys [id]}]
  (projects/save-project
    (merge
      #:user.project{:name  name
                     :owner owner
                     :code  {:html html
                             :css  css}}
      {:xt/id id})))

(defn project-editor [request]
  (let [lang (tr/lang request)
        logged-in? (some? (get-in request [:session :user]))
        project-id (util/get-path-param-as-uuid request :id)
        project (projects/find-user-project project-id)]
    (if (contains? project :user.project/html)
      (do
        (convert-project project)
        (response/redirect (tr/url lang "/projekt/editor/" project-id)))
      (editor/editor lang logged-in? (:user.project/code project) project-id))))


(defn save-project [request]
  (let [lang (tr/lang request)
        project-id (parse-uuid (get-in request [:form-params "id"]))
        html (get-in request [:form-params "html"])
        css (get-in request [:form-params "css"])
        project (projects/find-user-project project-id)]
    (println project-id)
    (println project)
    (println (:form-params request))
    (projects/save-project (merge project
                                  {:user.project/code {:html html
                                                       :css css}}))
    (response/redirect (tr/url lang "/projekt/editor/" project-id))))

(defn new-project-page [request]
  (let [lang (tr/lang request)
        logged-in? (some? (get-in request [:session :user]))
        templates (templates/find-all-templates)]
    (view/new-project-page lang logged-in? templates)))

(defn create-project-and-redirect [lang project]
  (let [project (projects/create-user-project project)]
    (response/redirect (tr/url lang "/projekt/editor/" (:xt/id project)))))

(defn post-new-project [request]
  (let [{:strs [project_name template]} (:form-params request)
        user-name (get-in request [:session :user :xt/id])
        lang (tr/lang request)]
    (if (= template "__empty__")
      (create-project-and-redirect lang #:user.project{:name project_name
                                                       :owner user-name})

      (let [template-id (parse-uuid template)
            template-data (templates/find-template template-id)]
        (println template-data)
        (create-project-and-redirect
          lang
          #:user.project{:name project_name
                         :owner user-name
                         :code (:template/code template-data)})))))

(def routes
  ["/:lang"
   ["/projekt"
    ["/editor/:id" {:get project-editor}]
    ["/neu" {:get  new-project-page
             :post post-new-project}]
    ["/speichern" {:post save-project}]]
   ["/projekte" {:get projects-page}]])