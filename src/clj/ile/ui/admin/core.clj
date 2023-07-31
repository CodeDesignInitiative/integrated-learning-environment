(ns ile.ui.admin.core
  (:require
    [clojure.string :as string]
    [ile.dictonary.translations :as tr]
    [ile.templates.core :as templates]
    [ile.ui.admin.view :as view]
    [ile.story.core :as story]
    [ile.util :as util]))

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

(defn mission-editor-page [request]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        mission (story/find-mission mission-id)]
    (view/edit-mission-page mission)))

(defn vector-from-string-lines [input]
  (if-not (empty? input)
    (->> input
         string/split-lines
         (remove empty?)
         (map string/trim)
         vec)
    []))

(defn map-mission-form
  [{:strs [mission_name _mission_world mission_step
           mission_story-before mission_story-after
           mission-content_mode-easy mission-content_mode-medium
           mission-content_mode-hard
           mission-content_easy mission-content_easy-wrong
           mission-content_medium mission-content_medium-wrong
           mission-content_hard mission-content_hard-wrong
           mission-content_hidden-html-easy mission-content_hidden-css-easy
           mission-content_hidden-html-medium mission-content_hidden-css-medium
           mission-content_hidden-html-hard mission-content_hidden-css-hard] :as params}]
  #:mission{:name         mission_name
            :world        :html-css                         ; (keyword mission_world)
            :step         (Integer/parseInt mission_step)
            :story-before (vector-from-string-lines mission_story-before)
            :story-after  (vector-from-string-lines mission_story-after)
            :content      [#:mission.content{:difficulty   :easy
                                             :mode         (keyword mission-content_mode-easy)
                                             :hidden-html  (or mission-content_hidden-html-easy "")
                                             :hidden-css   (or mission-content_hidden-css-easy "")
                                             :result       (vector-from-string-lines
                                                             mission-content_easy)
                                             :wrong-blocks (vector-from-string-lines
                                                             mission-content_easy-wrong)}
                           #:mission.content{:difficulty   :medium
                                             :mode         (keyword mission-content_mode-medium)
                                             :hidden-html  (or mission-content_hidden-html-medium "")
                                             :hidden-css   (or mission-content_hidden-css-medium "")
                                             :result       (vector-from-string-lines
                                                             mission-content_medium)
                                             :wrong-blocks (vector-from-string-lines
                                                             mission-content_medium-wrong)}
                           #:mission.content{:difficulty   :hard
                                             :mode         (keyword mission-content_mode-hard)
                                             :hidden-html  (or mission-content_hidden-html-hard "")
                                             :hidden-css   (or mission-content_hidden-css-hard "")
                                             :result       (vector-from-string-lines
                                                             mission-content_hard)
                                             :wrong-blocks (vector-from-string-lines
                                                             mission-content_hard-wrong)}]})

(defn mission-post-page [request]
  (let [mission-id (util/get-path-param-as-uuid request :id)
        mission (:form-params request)
        mission' (map-mission-form mission)]
    (if-not (nil? mission-id)
      (story/update-mission
        (merge {:xt/id mission-id} mission'))
      (story/create-mission mission'))
    (mission-editor-page request)))

(defn admin-page [_request]
  (view/admin-page))

(def routes
  ["/admin"
   ["" {:get admin-page}]
   ["/users" {:get users-page}]
   ["/templates" {:get templates-page}]
   ["/template/:id" {:get  template-edit-page
                     :post template-post-page}]
   ["/stories" {:get stories-page}]
   ["/story/:id" {:get  mission-editor-page
                  :post mission-post-page}]])