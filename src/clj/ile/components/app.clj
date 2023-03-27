(ns ile.components.app
  (:require [ile.layout :as layout]
            [ile.middleware :as middleware]
            [ile.ui :as ui]
            [xtdb.api :as xt]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.pprint :refer [pprint]]
            [ile.views.chat-screen :as chat-screen]
            [ile.views.wiki-screen :as wiki-screen]
            [ile.views.jobs-screen :as jobs-screen]
            [ile.views.editor-screen :as editor-screen]
            [ile.views.home-screen :as home-screen]
            [ile.views.settings-screen :as settings-screen]))

(defn app [{:keys [session] :as req}]
  (layout/render-page (home-screen/home-screen)))

(defn chat [{:keys [session] :as req}]
  (layout/render-page (chat-screen/chat-screen)))

(defn wiki [{:keys [session] :as req}]
  (layout/render-page (wiki-screen/wiki-screen)))

(defn jobs [{:keys [session] :as req}]
  (layout/render-page (jobs-screen/jobs-screen)))

(defn job-step [{:keys [session] :as req}]
  (layout/render-page (jobs-screen/job-step-screen (:params req))))

(defn editor [{:keys [session] :as req}]
  (layout/render-page
    (editor-screen/editor-screen
             [ile.core.mock-data/html-example
              ile.core.mock-data/css-example]
             []
             "")))

(defn settings [{:keys [session] :as req}]
  (layout/render-page (settings-screen/settings-screen)))
