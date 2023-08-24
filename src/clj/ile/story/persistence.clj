(ns ile.story.persistence
  (:require
    [clojure.spec.alpha :as s]
    [ile.persistence :as p]
    [ile.story.model]))

(defn create-mission [story-mission]
  (let [story-mission' (p/with-xt-id story-mission)]
    (if (s/valid? :ile/persistable-mission story-mission')
      (do (p/put-in-db-and-wait story-mission')
          {:status :success
           :data   story-mission'}
          )
      (do (s/explain :ile/persistable-mission story-mission')
          {:status :invalid-spec
           :data   (s/explain-data :ile/persistable-mission story-mission')}))))

(defn update-mission [story-mission]
  (if (s/valid? :ile/persistable-mission story-mission)
    (do (p/put-in-db-and-wait story-mission)
        story-mission)
    (s/explain :ile/persistable-mission story-mission)))

(defn find-all-missions []
  (->>
    (p/query-db '{:find  [(pull ?mission [* :ile/persistable-mission])]
                  :where [[?mission :mission/name any?]]})
    (sort-by :mission/step)
    vec))

(defn find-mission [mission-id]
  (p/find-first '{:find  [(pull ?mission [* :ile/persistable-mission])]
                  :where [[?mission :xt/id mission-id]]
                  :in [[mission-id]]}
                mission-id))


(defn find-next-mission [{:mission/keys [step world]}]
  (p/find-first '{:find [(pull ?mission [* :ile/persistable-mission])]
                  :where [[?mission :mission/step step]
                          [?mission :mission/world world]]
                  :in [[step world]]}
                (+ step 1) world))

(comment
  (create-mission
    #:mission{:world   :html-css
              :step    1
              :name    "Hallo Welt"
              :content [#:mission.content{:result     ["<h1>" "Hallo Welt!" "</h1>"]
                                          :difficulty :easy}]})

  (find-all-missions)



  (find-mission #uuid"2de20310-4330-4b33-90db-99234b4d6b49")
  (find-next-mission
    (find-mission #uuid"2de20310-4330-4b33-90db-99234b4d6b49"))
  )