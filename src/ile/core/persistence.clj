(ns ile.core.persistence
  (:require
    [ile.mount.xtdb :as xtdb]
    [xtdb.api :as xt]))

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

(defn find-first-by-id
  [id]
  (find-first '{:find  [(pull ?data [*])]
                :where [[?data :xt/id id]]
                :in    [[id]]}
              id))

(comment
  (find-first-by-id #uuid"bce63e65-7b46-4231-9135-45bf2c0a6475")
  )

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

;; learning tracks

(defn find-all-learning-tracks []
  (query-db '{:find  [(pull ?learning-track [* :ile/persistable-learning-track])]
              :where [[?learning-track :learning-track/name any?]]}))

(defn find-learning-track [learning-track-id]
  (find-first '{:find  [(pull ?learning-track [* :ile/persistable-learning-track])]
                :where [[?learning-track :xt/id learning-track-id]]
                :in    [[learning-track-id]]}
              learning-track-id))

(defn create-learning-tracks [learning-track]
  (put-in-db-and-wait (with-xt-id learning-track)))

(defn update-learning-tracks [learning-track]
  (put-in-db-and-wait learning-track))

(comment
  (find-all-learning-tracks)
  (find-learning-track #uuid"47a3a22a-f737-404f-b01a-2b2d223449fc")
  (->> (find-all-learning-tracks)
       (map #(:xt/id %))
       vec
       (apply remove-from-db))
  )