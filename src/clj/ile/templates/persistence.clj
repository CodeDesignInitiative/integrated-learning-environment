(ns ile.templates.persistence
  (:require
    [clojure.spec.alpha :as spec]
    [ile.persistence :as p]
    [ile.templates.model]))

(defn find-all-templates []
  (p/query-db '{:find [(pull ?templates [* :ile/template])]
                :where [[?templates :template/name any?]]}))

(defn find-template [id]
  (p/find-first '{:find [(pull ?templates [* :ile/template])]
                :where [[?templates :xt/id id]]
                :in [[id]]}
              id))

(defn create-template! [template]
  (let [template' (p/with-xt-id template)]
    (if (spec/valid? :ile/template template')
      (p/put-in-db-and-wait template')
      (spec/explain :ile/template template'))))

(defn update-template! [template]
  (if (spec/valid? :ile/template template)
    (p/put-in-db-and-wait template)
    (spec/explain :ile/template template)))

(comment
  (find-all-templates)

  ; creates new code tempalte
  (create-template! #:template{:name "Blog"
                               :code {:html "<h1>Hallo Welt</h2>"}
                               :visible? true})

  ; updates existing code tempalte
  (update-template! #:template{:name "Shop"
                               :code {:html "<h1>Hallo Welt!</h2>"}
                               :visible? true
                               :xt/id #uuid"ae446f97-3c9b-4c6f-8c9e-81562c103d72"})

  ; return spec error
  (create-template! #:template{:code {:html "<h1>Hallo Welt</h2>"}
                               :visible? true})
  )