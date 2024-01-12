(ns ile.ui.admin.core
  (:require
    [clojure.spec.alpha :as s]
    [clojure.walk :as walk]
    [ile.authentication.core :as authentication]
    [ile.middleware :as middleware]
    [ile.mount.config :refer [env]]
    [ile.story.core :as story]
    [ile.templates.core :as templates]
    [ile.ui.admin.view :as view]
    [ile.user.core :as user]
    [ile.core.util :as util]
    [ile.core.persistence :as persistence]
    [ile.core.models]
    [ile.core.processing :as processing]

    [clojure.java.io :as io]
    [clojure.string :as string]
    [ring.util.response :as response]))

(defn users-page [request]
  (view/users-page (authentication/is-admin? request)
                   (get-in request [:session :user])))

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
         #:mission{:name         {:de name}
                   :story-before {:de story-before}
                   :story-after  {:de story-after}
                   :content      {:de content}}))

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

(defn make-student [request]
  (let [user-id (util/get-path-param request :id)]
    (user/make-student user-id)))


(defn make-teacher [request]
  (let [user-id (util/get-path-param request :id)]
    (println user-id)
    (user/make-teacher user-id)))

(comment
  (:asset-store-path env))

(defn images-page [_request]
  (let [directory (clojure.java.io/file (:asset-store-path env))
        files (drop 1 (vec (file-seq directory)))]
    (view/images-page files)))

(defn file-path [file-name] (str (:asset-store-path env) "/" file-name))

(defn upload-image [request]
  (let [file-name (get-in request [:params :image-asset :filename])
        tmpfilepath (:path (bean (get-in request [:params :image-asset :tempfile])))]
    (io/copy (io/file tmpfilepath) (io/file (file-path file-name)))
    (images-page request)))

(defn backup-page [request]
  (let [missions (story/find-all-missions)
        templates (templates/find-all-templates)]
    [:div
     [:div (str missions)]
     [:div (str templates)]]))

(defn learning-tracks-page [request]
  (let [leraning-tracks (persistence/find-all-learning-tracks)]
    [:main.p-2
     [:nav.row
      [:a.button {:href "/admin"} "Back"]
      [:a.button {:href "/admin/learning-tracks/new"} "New Learning Track"]]
     [:ul.col.gap
      (map (fn [{:learning-track/keys [name visible? description language]
                 :xt/keys             [id]}] [:li
                                              [:a {:href (str "/admin/learning-track/" id)} name]
                                              [:p [:small description]]
                                              [:div "Lanuage: " (clojure.core/name language) " | Visble? " visible?]]) leraning-tracks)]]))


(defn learning-tracks-new-page [request]
  [:main
   [:a {:href "/admin/learning-tracks"} "<- Back"]
   [:form.col {:action "/admin/learning-tracks/new"
               :method :post}
    (util/hidden-anti-forgery-field)

    [:label "Name"]
    [:input {:id       "learning-track_name"
             :required true
             :name     "learning-track/name"}]

    [:label "Description"]
    [:textarea {:id       "learning-track_description"
                :required true
                :name     "learning-track/description"}]

    [:label "Language"]
    [:input {:id       "learning-track_language"
             :required true
             :name     "learning-track/language"}]
    [:small "Use the two letter ISO coutry code, like 'de', 'en',.. (https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes)"]

    [:button {:type :submit} "Create"]]])

(defn learning-tracks-edit-page [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        {:learning-track/keys [name description language visible?] :as learning-track}
        (persistence/find-learning-track learning-track-id)]
    [:<>
     [:nav
      [:a.button {:href (str "/admin/learning-track/" learning-track-id)} "<- Back"]]
     [:main#admin-page
      [:form.col {:action (str "/admin/learning-track/edit/" learning-track-id)
                  :method :post}
       (util/hidden-anti-forgery-field)

       [:label "Name"]
       [:input {:id       "learning-track_name"
                :required true
                :value    name
                :name     "learning-track/name"}]

       [:label "Description"]
       [:textarea {:id       "learning-track_description"
                   :required true
                   :value    description
                   :name     "learning-track/description"}]

       [:label "Language"]
       [:input {:id       "learning-track_language"
                :required true
                :value    (clojure.core/name language)
                :name     "learning-track/language"}]
       [:small "Use the two letter ISO coutry code, like 'de', 'en',.. (https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes)"]

       [:label "Visible?"]
       [:input {:name    "learning-track/visible?"
                :checked visible?
                :type    :checkbox}]

       [:button {:type :submit} "Update"]]]]))

(defn learning-tracks-detail-page [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        {:learning-track/keys [name description language visible?] :as learning-track}
        (persistence/find-learning-track learning-track-id)
        learning-track-tasks (group-by :learning-track-task/active? (persistence/find-learning-track-tasks learning-track-id))]
    (view/learning-tracks-detail-page learning-track learning-track-id learning-track-tasks)))

(defn learning-tracks-edit-page-posted [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        learning-track (processing/process-learning-track-edit (util/get-form-params request) learning-track-id)]
    (if (s/valid? :ile/learning-track learning-track)
      (do
        (persistence/update-learning-tracks learning-track)
        (response/redirect (str "/admin/learning-track/" learning-track-id)))
      (learning-tracks-edit-page request))))

(defn learning-tracks-new-page-posted [request]
  (let [learning-track (processing/process-learning-track (util/get-form-params request))]
    (if (s/valid? :ile/learning-track learning-track)
      (do
        (persistence/create-learning-tracks learning-track)
        (response/redirect "/admin/learning-tracks"))
      (learning-tracks-new-page request))))

(defn new-learning-track-task-page [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        learning-track (persistence/find-first-by-id learning-track-id)]
    (view/new-learning-track-task-page learning-track)))

(defn new-learning-track-task-page-posted [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        learning-track-task (assoc (util/get-form-params request)
                              :learning-track-task/learning-track learning-track-id)
        new-task (processing/process-learning-track-task learning-track-task)]
    (if (s/valid? :ile/learning-track-task new-task)
      (do
        (persistence/create-learning-track-task new-task)
        (response/redirect (str "/admin/learning-track/" learning-track-id)))
      (do
        (view/new-learning-track-task-page (persistence/find-first-by-id learning-track-id))))))

(defn edit-learning-track-task-page [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        learning-track (persistence/find-first-by-id learning-track-id)
        learning-track-task-id (util/get-path-param-as-uuid request :task-id)
        learning-track-task (persistence/find-first-by-id learning-track-task-id)]
    (view/new-learning-track-task-page learning-track learning-track-task))
  )

(defn edit-learning-track-task-page-posted [request]
  (let [learning-track-id (util/get-path-param-as-uuid request :id)
        learning-track-task-id (util/get-path-param-as-uuid request :task-id)
        learning-track-task (assoc (util/get-form-params request)
                              :learning-track-task/learning-track learning-track-id)
        new-task (processing/process-learning-track-task learning-track-task learning-track-task-id)]
    (if (s/valid? :ile/persistable-learning-track-task new-task)
      (do
        (persistence/update-learning-track-task new-task)
        (response/redirect (str "/admin/learning-track/" learning-track-id)))
      (do
        (clojure.pprint/pprint (s/explain :ile/persistable-learning-track new-task))
        (view/new-learning-track-task-page (persistence/find-first-by-id learning-track-id) new-task)))))

(def routes
  ["/admin" {:middleware [middleware/wrap-teacher-access]}
   ["" {:get admin-page}]
   ["/backup" {:get backup-page}]
   ["/images" {:get  images-page
               :post upload-image}]
   ["/users" {:get users-page}]
   ["/templates" {:get templates-page}]
   ["/template/:id" {:get  template-edit-page
                     :post template-post-page}]
   ["/learning-tracks" {:get learning-tracks-page}]
   ["/learning-tracks/new" {:get  learning-tracks-new-page
                            :post learning-tracks-new-page-posted}]
   ["/learning-track/:id" {:get learning-tracks-detail-page}]
   ["/learning-track/edit/:id" {:get  learning-tracks-edit-page
                                :post learning-tracks-edit-page-posted}]
   ["/learning-track/:id/tasks/new" {:get  new-learning-track-task-page
                                     :post new-learning-track-task-page-posted}]
   ["/learning-track/:id/task/:task-id" {:get  edit-learning-track-task-page
                                         :post edit-learning-track-task-page-posted}]
   ["/stories" {:get stories-page}]
   ["/story/:id/" {:get redirect-mission-editor-page}]
   ["/story/:id/:lang" {:get  mission-editor-page
                        :post mission-post-page}]])

(def htmx-routes
  ["/admin" {:middleware [middleware/wrap-admin-access]}
   ["/make-student/:id" {:post make-student}]
   ["/make-teacher/:id" {:post make-teacher}]])
