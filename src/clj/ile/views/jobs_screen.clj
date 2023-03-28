(ns ile.views.jobs-screen
  (:require [ile.views.chat-screen :as chat-screen]
            [ile.courses.html-website :as html-website]
            [ile.views.editor-screen :as editor-screen]))


(defn jobs-screen []
  [:main
   [:h1.p-7.text-xl "Aufträge"]
   [:div.flex.flex-row.m-7
    (map
      (fn [{:project/keys [id name tags chapters]}]
        [:div.p-7.bg-green-400.rounded
         [:h2.text-2xl.mb-3 name]
         [:a {:href (str "/auftrag?job=" id)} "Bearbeiten"]
         #_[:div.flex.flex-row
            (map (fn [t] [:span (str (name t))]) tags)]])
      ile.core.mock-data/projects)]])

(defn get-chapter-for-job [job step]
  (let [html-job (:project/chapters html-website/html-project)]
    (get html-job (- step 1))))


(defn job-step-screen [params]
  (let [job (get params :job)
        step (if (get params :step)
               (-> (get params :step) Integer/parseInt)
               1)
        show-chat (get params :chat)
        chapter (get-chapter-for-job job step)]
    (if show-chat
      (chat-screen/chat-screen (get-in chapter [:project.chapter/story])
                               (if (not-empty show-chat)
                                             (Integer/parseInt show-chat) 1)
                               job
                               step)
      (editor-screen/editor-screen (get-in chapter [:project.chapter/code])
                                   (:project.chapter/notes chapter)
                                   (if (< step (count (:project/chapters html-website/html-project)))
                                     (str "/auftrag?job=" job "&step=" (+ step 1) "&chat")
                                     "/"))

      )))