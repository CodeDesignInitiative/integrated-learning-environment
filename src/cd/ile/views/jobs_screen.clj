(ns cd.ile.views.jobs-screen
  (:require [cd.ile.views.editor-screen :as editor-screen]))


(defn jobs-screen []
  [:main.bg-gradient-to-b.text-white
   [:h1 "Aufträge"]
   "Hello there"])

(defn get-chapter-for-job [job step]
  (let [html-job (:project/chapters cd.ile.core.mock-data/html-project)]
    (get html-job (- step 1))))


(defn job-step-screen [params]
  (let [job (get params :job)
        step (-> (get params :step) Integer/parseInt)
        chapter (get-chapter-for-job job step)]
    (clojure.pprint/pprint chapter)
    [:div
     (editor-screen/editor-screen (get-in chapter [:project.chapter/code :html :code/line])
                                  (get-in chapter [:project.chapter/code :css :code/line])
                                  (:project.chapter/notes chapter)
                                  (str "/app/auftrag?job=" job "&step=" (+ step 1)))]))