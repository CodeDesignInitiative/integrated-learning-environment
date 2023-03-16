(ns cd.ile.views.jobs-screen
  (:require [cd.ile.views.chat-screen :as chat-screen]
            [cd.ile.views.editor-screen :as editor-screen]))


(defn jobs-screen []
  [:main.bg-gradient-to-b.text-white
   [:h1 "Aufträge"]
   (map
     (fn [{:project/keys [id name tags chapters]}]
       (println tags)
       [:div
        [:h2 name]
        [:a {:href (str "/app/auftrag?job=" id)} "Bearbeiten"]
        #_[:div.flex.flex-row
         (map (fn [t] [:span (str (name t))]) tags)]])
     cd.ile.core.mock-data/projects)])

(defn get-chapter-for-job [job step]
  (let [html-job (:project/chapters cd.ile.core.mock-data/html-project)]
    (get html-job (- step 1))))


(defn job-step-screen [params]
  (let [job (get params :job)
        step (if (get params :step)
               (-> (get params :step) Integer/parseInt)
               1)
        show-chat (get params :chat)
        chapter (get-chapter-for-job job step)]
    (clojure.pprint/pprint chapter)
    [:div
     (if show-chat
       (chat-screen/chat-interactive-screen (get-in chapter [:project.chapter/story])
                                            (if (not-empty show-chat)
                                              (Integer/parseInt show-chat) 1)
                                            job
                                            step)
       (editor-screen/editor-screen (get-in chapter [:project.chapter/code :html :code/line])
                                    (get-in chapter [:project.chapter/code :css :code/line])
                                    (:project.chapter/notes chapter)
                                    (str "/app/auftrag?job=" job "&step=" step "&chat")))]))