(ns ile.ui.editor.core
  (:require
    [ile.ui.editor.view :as view]))

(defn editor [lang logged-in? code project-id]
  (view/editor-page lang logged-in? code project-id))

(defn block-editor [lang mission learning-track-id story-mode?]
  (view/block-editor lang mission learning-track-id story-mode?))