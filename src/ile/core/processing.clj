(ns ile.core.processing)


(defn process-learning-track [learning-track]
  (-> learning-track
      (update :learning-track/language keyword)
      (assoc :learning-track/visible? false)))

(defn process-learning-track-edit [learning-track learning-track-id]
  (-> learning-track
      (assoc :xt/id learning-track-id)
      (update :learning-track/language keyword)
      (update :learning-track/visible? #(= % "on"))))