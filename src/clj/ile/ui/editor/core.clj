(ns ile.ui.editor.core
  (:require
    [ile.ui.editor.view :as view]))

(defn editor [code next]
  (view/editor-page code next))

;(defn new-project [request]
;  (let [template (get-in request [:form-params "template"])
;        project-name (get-in request [:form-params "project-name"])
;        user-email (-> (get-in request [:session :identity]) name)
;        template-code (get-template-code template)
;        project-id (random-uuid)]
;    (do
;      (persistence/create-user-project (merge {:xt/id project-id}
;                                              #:user.project{:name  project-name
;                                                             :owner user-email
;                                                             :css   (if template (:css template-code) "")
;                                                             :html  (if template (:html template-code) "")}))
;      (println "\n\nRedirect...\n\n")
;      (response/redirect (str "/projekt?id=" project-id)))))
;
;(defn project-editor [request]
;  (let [project-id (get-in request [:query-params "id"])
;        project (persistence/find-user-project (parse-uuid project-id))]
;    (ile.views.editor-screen/editor-screen
;      {:html {:code/line (:user.project/html project)}
;       :css  {:code/base (:user.project/css project)}}
;      nil nil)
;    ))
;
;
;(defn redirect-new-project [request]
;  (let [template (get-in request [:form-params "template"])
;        project-name (get-in request [:form-params "name"])
;        user-email (-> (get-in request [:session :identity]) name)
;        project-id (random-uuid)]
;
;    (persistence/create-user-project (merge {:xt/id project-id}
;                                            #:user.project{:name  project-name
;                                                           :owner user-email
;                                                           :css   (or (:css template) "")
;                                                           :html  (or (:html template) "")}))
;    (println "\n\nRedirect...\n\n")
;    (response/redirect (str "/projekt?id=" project-id))
;    ))


;(def routes
;  ["/projekte"
;   ["" {:get  editor-screen/editor-project-selection-screen
;        #_projects-page/projects-page
;        :post new-project}]
;   ["/neu" {:post new-project
;            :get  redirect-new-project}]
;   ])