(ns ile.core.view
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [ile.core.persistence :as p]
            [ile.core.util :as util]))

(defmulti form-field #(:input %))


(defmethod form-field :default [params]
  (clojure.pprint/pprint params)
  [:p "Unkown input field type"]
  )
(defmethod form-field :string [{:keys [label spec-key hint deserialize-fn]}]
  [:<>
   [:label {:for (name spec-key)} label]
   [:input {:name (name spec-key)}]
   [:small hint]])

(defmethod form-field :checkbox [{:keys [label spec-key hint]}]
  [:<>
   [:label {:for (name spec-key)} label]
   [:input {:type :checkbox
            :name (name spec-key)}]
   [:small hint]])

(defn render-html-form [{:keys [heading path fields] :as form-component} request & [errors]]
  (let [xt-id (if (util/has-path-param? request :id)
                (util/get-path-param-as-uuid request :id)
                nil)
        form-data (when xt-id (p/find-first-by-id xt-id))]
    [:form {:action (if xt-id
                      (str path "/" xt-id)
                      (str path "/new"))}
     [:h2 heading]
     (map form-field fields)

     [:button {:type :submit} (if xt-id "Update" "Create")]]))

(defn process-form-component-submission [{:keys [spec callback-fn] :as form-component} request]
  (let [form-params (walk/keywordize-keys (:form-params request))
        xt-id (util/get-path-param-as-uuid request :id)]
    (if (s/valid? spec form-params)
      (if xt-id
        (callback-fn (merge {:xt/id xt-id} form-params))
        (callback-fn form-params))
      (render-html-form form-component request (s/explain spec form-params)))))


(comment
  (def test-component
    {:heading     "Learning Track Task"
     :path        "/admin/learning-track"
     :spec        :ile/learning-track-task
     :callback-fn clojure.pprint/pprint
     :deletable?  true
     :fields
     [{:input    :string
       :spec-key :learning-track-task/name}
      {:input    :number
       :spec-key :learning-track-task/step
       :label    "Step"
       :hint     "Step numbers must be continous. Level ends, when no higher number which is 1 higher than the current is found."}
      {:input    :checkbox
       :spec-key :learning-track-task/active?
       :label    "Active task?"
       :hint     "Toggle if task is visible to users. Hide unfinished or outdated tasks."}
      {:input    :textarea
       :spec-key :learning-track-task/messages-before
       :label    "Message(s) shown before the task"
       :optional true
       :hint     "If the learning-track is in story mode, each new line is a message. Else it is presented as one text block. Use markdown to apply formatting."}
      {:input    :select
       :spec-key :learning-track-task/input-type
       :options  [{:id    :block
                   :label "Block Editor Mode"}
                  {:id    :text
                   :label "Text Editor Mode"}]}]})

  (def learning-track-component
    {:heading     "Learning Track"
     :path        "/admin/learning-track"
     :spec        :ile/learning-track-task
     :callback-fn clojure.pprint/pprint
     :fields
     [{:input    :string
       :spec-key :learning-track/name
       :label    "Name"}
      {:input    :string
       :spec-key :learning-track/description
       :label    "Description"}
      {:input           :string
       :spec-key        :learning-track/description
       :label           "Language"
       :hint            "Use the two letter ISO coutry code, like 'de', 'en',.. (https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes)"
       :serialize-fn    keyword
       :deserialzie-fn  name
       :form-field-opts {:max-length 2
                         :min-length 2}}
      {:input    :checkbox
       :spec-key :learning-track/visible?
       :label    "Visible learning track?"
       :hint     "Toggle if learning track should be visible to users. Hide unfinished or outdated learning tracks."}
      ]})


  (render-html-form learning-track-component {})


  (render-html-form learning-track-component {:path-params {:id "bce63e65-7b46-4231-9135-45bf2c0a6475"}})

  )

#_(def learning-track-task-component
    {:heading "Learning Track Task"
     :path    "/admin/learning-track"
     :spec    :ile/learning-track-task
     :fields
     [{:input :string
       :key   :learning-track-task/name}
      {:input :string
       :key   :learning-track-task/explanation}
      {:input          :select
       :key            :learning-track-task/learning-track
       :options        (persistence/find-all-learning-tracks)
       :option-mapping {:label [:learning-track/name :learning-track/language]
                        :value :xt/id}}
      {:input :number
       :key   :learning-track-task/step
       :label "Step"
       :hint  "Step numbers must be continous. Level ends, when no higher number which is 1 higher than the current is found."}
      {:input :checkbox
       :key   :learning-track-task/active?
       :label "Active task?"
       :hint  "Toggle if task is visible to users. Hide unfinished or outdated tasks."}
      {:input    :textarea
       :key      :learning-track-task/messages-before
       :label    "Message(s) shown before the task"
       :optional true
       :hint     "If the learning-track is in story mode, each new line is a message. Else it is presented as one text block. Use markdown to apply formatting."}
      {:input    :textarea
       :key      :learning-track-task/messages-after
       :label    "Message(s) shown after the task"
       :optional true
       :hint     "If the learning-track is in story mode, each new line is a message. Else it is presented as one text block. Use markdown to apply formatting."}
      {:input :textarea
       :key   :learning-track-task/solution
       :label "Correct Solution for the task"
       :hint  "Depending on the input mode (HTML, CSS, JS) provide the correct solution for the current task."}
      {:input    :textarea
       :key      :learning-track-task/hint
       :label    "Hint for current task"
       :optional true
       :hint     "Shows content as additional help, if ?-button is clicked"}
      {:input    :textarea
       :key      :learning-track-task/hidden-html
       :label    "Hidden HTML Code"
       :optional true
       :hint     "HTML code that is not visible to the user. Insert the text '$$placeholder$$' for the place where user code should be injected."}
      {:input    :textarea
       :key      :learning-track-task/hidden-css
       :label    "Hidden CSS Code"
       :optional true
       :hint     "CSS code that is not visible to the user. Insert the text '$$placeholder$$' for the place where user code should be injected."}
      {:input    :textarea
       :key      :learning-track-task/hidden-js
       :label    "Hidden JS Code"
       :optional true
       :hint     "JS code that is not visible to the user. Insert the text '$$placeholder$$' for the place where user code should be injected."}

      {:input   :select
       :key     :learning-track-task/input-type
       :options [{:id    :block
                  :label "Block Editor Mode"}
                 {:id    :text
                  :label "Text Editor Mode"}]}
      {
       :learning-track-task/mode         (s/coll-of #{:html :css :js})
       :learning-track-task/wrong-blocks vector?
       :learning-track-task/visible?     boolean?}]}
    )