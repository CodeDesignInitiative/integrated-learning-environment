(ns ile.user.persistence
  (:require
    [clojure.string :as string]
    [crypto.password.bcrypt :as password]
    [ile.persistence :as p])
  )


(defn find-user-by-email
  [email]
  (p/find-first '{:find  [(pull ?user [* :ile/user])]
                  :where [(or [?user :xt/id email]
                              [?user :user/email email])]
                  :in    [[email]]}
                email))


(defn find-user-by-name
  [id]
  (p/find-first '{:find  [(pull ?user [* :ile/user])]
                  :where [(or [?user :user/name id])]
                  :in    [[id]]}
                id))

(defn find-all-users
  []
  (p/query-db '{:find  [(pull ?user [* :ile/user])]
                :where [[?user :user/password any?]]}))

(defn create-user
  [{:xt/keys [id] :as user}]
  (if (find-user-by-name id)
    :user-already-exists
    (do (p/put-in-db-and-wait user)
        user)))

(defn convert-users []
  (let [users [{:user/password "$2a$11$098vOTju1aLnA.S0R.Hw0ufkC07bKBS.n/Vncb52SKtIC7aW1Poqu", :user/mail "", :xt/id "horsti69"}] #_(find-all-users)]
    (->>
      users
      (map
        (fn [{:user/keys [password name email mail]
              :xt/keys   [id] :as user}]
          (let [password' (if (string/starts-with? password "$2a$11")
                            password
                            (password/encrypt password))]
            (if mail
              {:xt/id         id
               :user/email    mail
               :user/password password'}
              (if (empty? name)
                {:xt/id         id
                 :user/password password'
                 :user/email    id}
                (if (empty? email)
                  {:xt/id         name
                   :user/password password'
                   :user/email    id}
                  (merge user {:user/password password'})))))))
      (apply p/put-in-db))))

(comment
  (find-all-users)

  (find-user-by-name "test")

  (convert-users)

  (p/put-in-db {:user/password "$2a$11$ABaDeXe5vCcHDoBInVJeu.jAPA6qc81TSOKyx87VDrbsUaqXPzmJe",
                :user/name     "Paul",
                :xt/id         "paul.freelancing@posteo.de"}
               {:user/password "12345678", :user/name "", :xt/id "paul.hempel@posteo.de"}
               {:user/password "test1234", :user/name "", :xt/id "asdf@movie.de"}
               {:user/password "$2a$11$098vOTju1aLnA.S0R.Hw0ufkC07bKBS.n/Vncb52SKtIC7aW1Poqu", :user/mail "", :xt/id "horsti69"}
               {:user/password "$2a$11$WKyRKzVXneDvHrYknWtQaeoK3xwVeH9z/G1auKZxr/Dvhp37r/fda",
                :user/name     "test",
                :xt/id         "test@test.de"}
               {:user/password "$2a$11$EZEIva7ENoc1CkM7xB7J1O4WbSeetkm4G0ANb1SpfG.ZH3aE0Kg2i",
                :user/name     "Paul",
                :xt/id         "qwer@asdf.de"})

  (p/remove-from-db "paul.freelancing@posteo.de"
                    "paul.hempel@posteo.de"
                    "asdf@movie.de"
                    "horsti69"
                    "Paul"
                    "test"
                    "test@test.de"
                    "qwer@asdf.de")
  )