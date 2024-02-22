(ns ile.core.processing)


(defn process-learning-track [learning-track]
  (-> learning-track
      (update :learning-track/language keyword)
      (update :learning-track/story-mode? #(= % "on"))
      (assoc :learning-track/visible? false)))

(defn- parse-editor-modes [v]

  )

(comment
  (update {:learning-track-task/editor-modes ["html" "css"]}
          :learning-track-task/editor-modes
          (fn [v] (if (string? v)
                    [(keyword v)]
                    (vec (map keyword v)))))
  )


(defn process-learning-track-task [learning-track-task & [id]]
  (clojure.pprint/pprint learning-track-task)
  (-> learning-track-task
      (update :learning-track-task/step #(Integer/parseInt %))
      (update :learning-track-task/editor-modes (fn [v] (if (string? v)
                                                          [(keyword v)]
                                                          (vec (map keyword v)))))
      (update :learning-track-task/active? #(= % "on"))
      (update :learning-track-task/block-mode? #(= % "on"))
      (cond-> id (assoc :xt/id id))))

(defn process-learning-track-edit [learning-track learning-track-id]
  (-> learning-track
      (assoc :xt/id learning-track-id)
      (update :learning-track/language keyword)
      (update :learning-track/visible? #(= % "on"))
      (update :learning-track/story-mode? #(= % "on"))))