(ns ile.views.projects
  (:require [ile.persistence :as persistence]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))
