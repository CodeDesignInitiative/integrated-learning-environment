(ns ile.projects.persistence
  (:require [ile.persistence :as p]))


(defn create-user-project
  [user-project]
  (let [user-project' (p/with-xt-id user-project)]
    (p/put-in-db-and-wait user-project')
    user-project'))

(defn find-user-projects [user-email]
  (p/query-db '{:find  [(pull ?user-project [* :ile/user.project])]
                :where [[?user-project :user.project/owner user-email]]
                :in    [[user-email]]}
              user-email))

(defn find-user-project [project-id]
  (p/find-first '{:find  [(pull ?user-project [* :ile/user.project])]
                  :where [[?user-project :xt/id project-id]]
                  :in    [[project-id]]}
                project-id))

(comment
  (p/query-db '{:find  [(pull ?user-project [* :ile/user.project])]
                :where [[?user-project :user.project/owner any]]
                })
  #_  (->>
    (p/query-db '{:find  [(pull ?user-project [* :ile/user.project])]
                  :where [[?user-project :user.project/owner any?]]})
    (map #(:xt/id %))
    vec
    (apply p/remove-from-db))
  (find-user-projects "test@test.de")
  (find-user-project #uuid"ba24ebdc-00af-41df-ae4e-b1406d442926")
  )

(defn save-project [project]
  (p/put-in-db-and-wait project))