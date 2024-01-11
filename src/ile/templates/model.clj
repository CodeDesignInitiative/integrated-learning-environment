(ns ile.templates.model
  (:require [clojure.spec.alpha :as s]))

(s/def :template/name string?)
(s/def :template/name-translations map?)
(s/def :template/code map?)
(s/def :template/visible? boolean?)

(s/def :ile/template
  (s/keys :req [:xt/id
                :template/name
                :template/code
                :template/visible?]
          :opt [:template/name-translations]))
