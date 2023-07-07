(ns ile.persistence
  (:require
    [ile.mount.xtdb :as xtdb]
    [xtdb.api :as xt]
    [crypto.password.bcrypt :as password]
    ))

(defn- flatten-result
  "Takes the XTDB typical result (a set containing vectors) and combines the
  entries into a single vector"
  [result]
  (vec (mapcat (fn [e] e) result)))

(defn- query-args-count-match?
  "Checks whether the number of arguments required for the query match the provided
  number of arguments.

  Based on: https://github.com/jacobobryant/biff - Copyright (c) 2023 Jacob O'Bryan
  License:  https://github.com/jacobobryant/biff/blob/master/LICENSE"
  [query args]

  (when-not (= (count (first (:in query)))
               (count args))
    (throw (ex-info (str "Incorrect number of query arguments. Expected "
                         (count (first (:in query)))
                         " but got "
                         (count args)
                         ".")
                    {}))))

(defn query-db
  "Executes a xtdb query with the provided arguments and returns a flattened result vector."
  ([query & args]
   (query-args-count-match? query args)
   (-> (xt/db xtdb/node)
       (xt/q query (when args (vec args)))
       flatten-result))
  ([query]
   (-> (xt/db xtdb/node)
       (xt/q query)
       flatten-result)))

(defn find-first
  [query & args]
  (-> (apply query-db query args)
      first))

(defn put-in-db [& documents]
  (let [puts (vec (map (fn [v] [::xt/put v]) documents))]
    (xt/submit-tx xtdb/node puts)))

(defn put-in-db-and-wait [& documents]
  (xt/await-tx xtdb/node (apply put-in-db documents)))


(defn with-xt-id
  "Attaches the user-id to a document"
  [document]
  (merge document {:xt/id (random-uuid)}))

;; ----------------------------------------------- ;;

;; refactor below to own persistence NS
;;

(defn create-user
  [user]
  (put-in-db-and-wait user)
  user)

(defn find-user
  [email]
  (find-first '{:find  [(pull ?user [* :ile/user])]
                :where [[?user :xt/id email]]
                :in    [[email]]}
              email))

(defn create-user-project
  [user-project]
  (put-in-db-and-wait user-project))

(defn get-user-projects [user-email]
  (query-db '{:find [(pull ?user-project [* :ile/user.project])]
              :where [[?user-project :user.project/owner user-email]]
              :in [[user-email]]}
            user-email))

(defn find-user-project [project-id]
  (find-first '{:find [(pull ?user-project [* :ile/user.project])]
              :where [[?user-project :xt/id project-id]]
              :in [[project-id]]}
              project-id))

(comment
  (get-user-projects "paul.freelancing@posteo.de")
  (find-user "paul.freelancing@posteo.de")
  (find-user "asdf@movie.de")
  (find-user-project #uuid"ba24ebdc-00af-41df-ae4e-b1406d442926")
  (password/check "12345678" "$2a$11$ABaDeXe5vCcHDoBInVJeu.jAPA6qc81TSOKyx87VDrbsUaqXPzmJe")
  (put-in-db-and-wait {:user/password (password/encrypt "12345678"), :user/name "Paul", :xt/id {:user/email "paul.freelancing@posteo.de"}})
  )

(defn save-project [project]
  (put-in-db-and-wait project))