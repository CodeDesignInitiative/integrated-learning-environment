(ns ile.ui.legal.core
  (:require
    [ile.ui.legal.view :as view]))

(defn privacy-statement [request]
  (view/privacy-statement request))
