(ns ile.user.persistence
  (:require
    [clojure.string :as string]
    [crypto.password.bcrypt :as password]
    [ile.core.persistence :as p])
  )


(defn find-user-by-email
  [email]
  (p/find-first '{:find  [(pull ?user [* :ile/user])]
                  :where [(or [?user :xt/id email]
                              [?user :user/email email])]
                  :in    [[email]]}
                email))


(defn find-user-by-id
  [id]
  (p/find-first '{:find  [(pull ?user [* :ile/user])]
                  :where [(or [?user :xt/id id])]
                  :in    [[id]]}
                id))

(defn find-all-users
  []
  (p/query-db '{:find  [(pull ?user [* :ile/user])]
                :where [[?user :user/password any?]]}))

(defn create-user
  [{:xt/keys [id] :as user}]
  (if (find-user-by-id id)
    :user-already-exists
    (do (p/put-in-db-and-wait user)
        user)))


(comment
  (find-all-users)

  (dissoc (find-user-by-id "paule") :user/roles)

  (p/remove-from-db "paule")

  (p/put-in-db {:xt/id         "root"
                ; password is "rootroot", just encrypted
                :user/password "$2a$11$CY6BFz9uHSSBZ2o9Q.NMx.dulI7qdrmCt2zLHNgU3vheiv5y3hdvG"
                :user/email    "paul.hempel@code.design"
                :user/roles    [:admin]}
               )

  (p/remove-from-db "root")
  )


(defn make-student [user-id]
  (let [user (find-user-by-id user-id)]
    (when-not (some #{:admin} (:user/roles user))
      (p/put-in-db-and-wait (dissoc user :user/roles)))))


(defn make-teacher [user-id]
  (let [user (find-user-by-id user-id)]
    (when-not (some #{:admin} (:user/roles user))
      (p/put-in-db-and-wait (merge user {:user/roles [:teacher]})))))