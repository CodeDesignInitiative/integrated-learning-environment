(ns ile.ui.admin.core
  (:require
    [clojure.string :as string]
    [ile.dictonary.translations :as tr]
    [ile.templates.core :as templates]
    [ile.ui.admin.view :as view]
    [ile.story.core :as story]
    [ile.util :as util]
    [ile.middleware :as middleware]
    [ring.util.response :as response]))

(defn users-page [request]
  (view/users-page))

(defn templates-page [request]
  (let [templates (templates/find-all-templates)]
    (view/templates-page templates)))

(defn template-edit-page [request]
  (let [template-id (util/get-path-param-as-uuid request :id)
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
  (let [template-id (util/get-path-param-as-uuid request :id)
        template (:form-params request)
        template' (map-template-form template)]
    (if-not (nil? template-id)
      (templates/update-template!
        (merge {:xt/id template-id} template'))
      (templates/create-template! template'))
    (template-edit-page request)))

(defn stories-page [_request]
  (let [missions (story/find-all-missions)]
    (view/story-page (sort-by :mission/step (group-by :mission/world missions)))))

(defn- migrate-mission [{:mission/keys [name story-before story-after content] :as mission}]
  (merge mission
         #:mission{:name {:de name}
                   :story-before {:de story-before}
                   :story-after {:de story-after}
                   :content {:de content}}))

(defn mission-editor-page [request & [error]]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        lang (keyword (util/get-path-param request :lang))
        mission (story/find-mission mission-id)]
    (view/edit-mission-page mission lang (when error error))))

(defn vector-from-string-lines [input]
  (if (not-empty input)
    (->> input
         string/split-lines
         (remove empty?)
         (map string/trim)
         vec)
    []))

(defn map-mission-form
  [{:strs [mission_name _mission_world mission_step
           mission_story-before mission_story-after
           mission-content_input-type-easy
           mission-content_input-type-medium
           mission-content_input-type-hard
           mission-content_hint-easy
           mission-content_hint-medium
           mission-content_hint-hard
           mission-content_explanation-easy
           mission-content_explanation-medium
           mission-content_explanation-hard
           mission-content_mode-easy
           mission-content_mode-medium
           mission-content_mode-hard
           mission-content_easy mission-content_easy-wrong
           mission-content_medium mission-content_medium-wrong
           mission-content_hard mission-content_hard-wrong
           mission-content_hidden-html-easy mission-content_hidden-css-easy
           mission-content_hidden-html-medium mission-content_hidden-css-medium
           mission-content_hidden-html-hard mission-content_hidden-css-hard] :as params}
   lang]
  #:mission{:name         {lang mission_name}
            :world        :html-css                         ; (keyword mission_world)
            :step         (Integer/parseInt mission_step)
            :story-before {lang (vector-from-string-lines mission_story-before)}
            :story-after  {lang (vector-from-string-lines mission_story-after)}
            :content      {lang [#:mission.content{:difficulty   :easy
                                                   :hint         mission-content_hint-easy
                                                   :explanation
                                                   mission-content_explanation-easy
                                                   :mode         (keyword mission-content_mode-easy)
                                                   :input-type   (keyword
                                                                   mission-content_input-type-easy)
                                                   :hidden-html  (or mission-content_hidden-html-easy "")
                                                   :hidden-css   (or mission-content_hidden-css-easy "")
                                                   :result       (vector-from-string-lines
                                                                   mission-content_easy)
                                                   :wrong-blocks (vector-from-string-lines
                                                                   mission-content_easy-wrong)}
                                 #:mission.content{:difficulty   :medium
                                                   :hint         mission-content_hint-medium
                                                   :explanation
                                                   mission-content_explanation-medium
                                                   :mode         (keyword mission-content_mode-medium)
                                                   :input-type   (keyword
                                                                   mission-content_input-type-medium)
                                                   :hidden-html  (or mission-content_hidden-html-medium "")
                                                   :hidden-css   (or mission-content_hidden-css-medium "")
                                                   :result       (vector-from-string-lines
                                                                   mission-content_medium)
                                                   :wrong-blocks (vector-from-string-lines
                                                                   mission-content_medium-wrong)}
                                 #:mission.content{:difficulty   :hard
                                                   :hint         mission-content_hint-hard
                                                   :explanation
                                                   mission-content_explanation-hard
                                                   :mode         (keyword mission-content_mode-hard)
                                                   :input-type   (keyword
                                                                   mission-content_input-type-hard)
                                                   :hidden-html  (or mission-content_hidden-html-hard "")
                                                   :hidden-css   (or mission-content_hidden-css-hard "")
                                                   :result       (vector-from-string-lines
                                                                   mission-content_hard)
                                                   :wrong-blocks (vector-from-string-lines
                                                                   mission-content_hard-wrong)}]}})

(defn merge-mission [mission new-mission]
  (-> (merge-with merge
                  (dissoc mission :mission/world :mission/step :xt/id)
                  (dissoc new-mission :mission/world :mission/step))
      (assoc :mission/world (:mission/world new-mission))
      (assoc :mission/step (:mission/step new-mission))
      (assoc :xt/id (:xt/id mission))))

(defn mission-post-page [request]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        lang (keyword (util/get-path-param request :lang))
        mission (story/find-mission mission-id)
        posted-mission (:form-params request)
        posted-mission' (map-mission-form posted-mission lang)]
    (if mission-id
      (do
        (story/update-mission (merge-mission mission posted-mission'))
        (mission-editor-page request))
      (let [{:keys [status data]} (story/create-mission posted-mission')]
        (condp = status
          :success
          (doall
            (response/redirect (str "/admin/story/" (:xt/id data) "/" (name lang))))
          :invalid-spec
          (mission-editor-page request))))))

(defn admin-page [_request]
  (view/admin-page))

(defn redirect-mission-editor-page [request]
  (response/redirect (str (:uri request) "/de")))

(def routes
  ["/admin" {:middleware [#_middleware/admin-access]}
   ["" {:get admin-page}]
   ["/users" {:get users-page}]
   ["/templates" {:get templates-page}]
   ["/template/:id" {:get  template-edit-page
                     :post template-post-page}]
   ["/stories" {:get stories-page}]
   ["/story/:id/" {:get redirect-mission-editor-page}]
   ["/story/:id/:lang" {:get  mission-editor-page
                        :post mission-post-page}]])