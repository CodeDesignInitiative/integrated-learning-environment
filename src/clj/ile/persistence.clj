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

(defn remove-from-db
  [& documents]
  (let [puts (vec (map (fn [v] [::xt/delete v]) documents))]
    (xt/submit-tx xtdb/node puts)))

(defn with-xt-id
  "Attaches the user-id to a document"
  [document]
  (merge document {:xt/id (random-uuid)}))

;; ----------------------------------------------- ;;

;; refactor below to own persistence NS
;;


(defn find-user
  [email]
  (find-first '{:find  [(pull ?user [* :ile/user])]
                :where [(or [?user :xt/id email]
                            [?user :user/email email])]
                :in    [[email]]}
              email))

(defn create-user
  [{:xt/keys [id] :as user}]
  (if (find-user id)
    :user-already-exists
    (do (put-in-db-and-wait user)
        user))
  )


(defn get-user-password [a]
  (get a :user/password))

(defn render-html-page [{:keys [name _age height] :as _user}]
  [:main
   [:h1 "User"]
   [:p (str "Name: " name)]
   [:p (str "Height: " height)]
   (when (> height 150)
     [:h2 "Success, you are not a Hobbit!"])])

(comment

  (def user-marv {:name   "Marvin"
                  :age    "Forever Young"
                  :height 180})

  (render-html-page user-marv)

  )
