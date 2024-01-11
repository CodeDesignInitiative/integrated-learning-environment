(ns ile.core.models
  (:require [clojure.spec.alpha :as s]))

(s/def :learning-track/name (s/and string? not-empty))
(s/def :learning-track/language keyword?)
(s/def :learning-track/description (s/and string? not-empty))
(s/def :learning-track/visible? boolean?)

(s/def :ile/learning-track
  (s/keys :req [:learning-track/name
                :learning-track/language
                :learning-track/description]
          :opt [:learning-track/visible?]))

(comment
  (s/describe :ile/learning-track)
  )

(s/def :ile/persistable-learning-track
  (s/merge :ile/learning-track
           (s/keys :req [:xt/id])))

(s/def :learning-track-task/name string?)
(s/def :learning-track-task/explanation string?)
(s/def :learning-track-task/learning-track uuid?)
(s/def :learning-track-task/step int?)
(s/def :learning-track-task/active? boolean?)
(s/def :learning-track-task/messages-before string?)
(s/def :learning-track-task/messages-after string?)
(s/def :learning-track-task/solution string?)
(s/def :learning-track-task/hint string?)
(s/def :learning-track-task/hidden-html string?)
(s/def :learning-track-task/hidden-css string?)
(s/def :learning-track-task/hidden-js string?)
(s/def :learning-track-task/input-type #{:block :text})
(s/def :learning-track-task/mode (s/coll-of #{:html :css :js}))
(s/def :learning-track-task/wrong-blocks string?)
(s/def :learning-track-task/visible? boolean?)

(comment
  (s/valid? :learning-track-task/mode [:html :css])
  )

(s/def :ile/learning-track-task
  (s/keys :req [:learning-track-task/name
                :learning-track-task/explanation
                :learning-track-task/learning-track
                :learning-track-task/step
                :learning-track-task/result]
          :opt [:learning-track-task/story-after
                :learning-track-task/story-before
                :learning-track-task/hint
                :learning-track-task/hidden-html
                :learning-track-task/hidden-css
                :learning-track-task/hidden-js
                :learning-track-task/input-type
                :learning-track-task/mode
                :learning-track-task/wrong]))

(s/def :ile/persistable-mission
  (s/merge :ile/mission
           (s/keys :req [:xt/id])))