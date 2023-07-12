(ns ile.ui.editor.core
  (:require
    [ile.ui.editor.view :as view]))

(defn editor [lang code project-id]
  (view/editor-page lang code project-id))

(defn block-editor [lang mission]
  (view/block-editor lang mission))