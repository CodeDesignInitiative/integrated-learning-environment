(ns ile.core.models
  (:require [clojure.spec.alpha :as s]))

(s/def :learning-track/name (s/and string? not-empty))
(s/def :learning-track/language keyword?)
(s/def :learning-track/description (s/and string? not-empty))
(s/def :learning-track/visible? boolean?)
(s/def :learning-track/story-mode? boolean?)

(s/def :ile/learning-track
  (s/keys :req [:learning-track/name
                :learning-track/language
                :learning-track/description]
          :opt [:learning-track/visible?
                :learning-track/story-mode?]))

(comment
  (s/describe :ile/learning-track)
  (s/valid? :learning-track/name "")
  )

(s/def :ile/persistable-learning-track
  (s/merge :ile/learning-track
           (s/keys :req [:xt/id])))

(s/def :learning-track-task/name string?)
(s/def :learning-track-task/explanation string?)
(s/def :learning-track-task/learning-track uuid?)
(s/def :learning-track-task/step int?)
(s/def :learning-track-task/solution-html string?)
(s/def :learning-track-task/solution-css string?)
(s/def :learning-track-task/solution-js string?)
(s/def :learning-track-task/editor-modes (s/coll-of #{:html :css :js} :distinct true :min-count 1 :max-count 3))
(s/def :learning-track-task/active? boolean?)
(s/def :learning-track-task/messages-before string?)
(s/def :learning-track-task/messages-after string?)
(s/def :learning-track-task/hint string?)
(s/def :learning-track-task/visible-html string?)
(s/def :learning-track-task/hidden-html string?)
(s/def :learning-track-task/visible-css string?)
(s/def :learning-track-task/hidden-css string?)
(s/def :learning-track-task/visible-js string?)
(s/def :learning-track-task/hidden-js string?)
(s/def :learning-track-task/block-mode? boolean?)
(s/def :learning-track-task/wrong-blocks string?)

(comment
  (s/valid? :learning-track-task/editor-modes [:html :css :js])
  (s/valid? :learning-track-task/editor-modes [:html :css :js :js :js :js])
  (s/valid? :learning-track-task/editor-modes ())
  (s/valid? :learning-track-task/editor-modes [:nil])
  )

(s/def :ile/learning-track-task
  (s/keys :req [:learning-track-task/name
                :learning-track-task/explanation
                :learning-track-task/learning-track
                :learning-track-task/step
                :learning-track-task/editor-modes]
          :opt [:learning-track-task/active?
                :learning-track-task/solution-html
                :learning-track-task/solution-css
                :learning-track-task/solution-js
                :learning-track-task/messages-before
                :learning-track-task/messages-after
                :learning-track-task/hint
                :learning-track-task/visible-html
                :learning-track-task/hidden-html
                :learning-track-task/visible-css
                :learning-track-task/hidden-css
                :learning-track-task/visible-js
                :learning-track-task/hidden-js
                :learning-track-task/block-mode?
                :learning-track-task/wrong-blocks]))

(s/def :ile/persistable-learning-track-task
  (s/merge :ile/learning-track-task
           (s/keys :req [:xt/id])))

(s/def :ile/persistable-mission
  (s/merge :ile/mission
           (s/keys :req [:xt/id])))