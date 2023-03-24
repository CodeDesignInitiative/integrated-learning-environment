(ns ile.middleware.formats
  (:require
    [muuntaja.core :as m]))

(def instance
  (m/create m/default-options))
