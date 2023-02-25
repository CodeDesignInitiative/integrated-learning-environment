(ns cd.ile.schema
  (:require [malli.core :as malc]
            [malli.registry :as malr]))

(def schema
  {; user save state storing active and done projects
   :user.progress/current-projects  [:vector :uuid]
   :user.progress/finished-projects [:vector :uuid]

   :user/id                    :uuid
   :user/email                 :string
   :user/joined-at             inst?
   ; application user for login and state
   :user                       [:map {:closed true}
                                [:xt/id :user/id]
                                :user/email
                                :user/joined-at
                                [:map {:closed true}
                                 :user.progress/current-projects
                                 :user.progress/finished-projects]]


   :code/lang                  keyword?
   :code/base                  :string
   :code/snippet               :string
   :code/line                  :string
   ; the code for each chapter of a project
   :code                       [:map {:closed true}
                                :code/lang
                                :code/base
                                :code/snippet
                                :code/line]

   :person/id                  :uuid
   :person/name                :string
   :person/organisation        :string
   ; person which can appear in a story
   :person                     [:map {:closed true}
                                [:xt/id :person/id]
                                :person/name
                                :person/organisation]


   :story/person               [:or :person :keyword]
   :story/message              :string
   :story/video                :string
   ; chat message part of a projects chapter
   :story                      [:map {:closed true}
                                :story/person
                                :story/message
                                [:story/video {:optional true}]]


   :project/id                 :uuid
   :project/name               :string
   :project/tags               [:vector keyword?]
   ; project wich represents a programming tutorial
   :project                    [:map {:closed true}
                                [:xt/id :project/id]
                                :project/name
                                :project/tags]

   :project.chapter/id         :uuid
   :project.chapter/name       :string
   :project.chapter/notes      [:vector :string]
   :project.chapter/wiki-ref   :uuid
   :project.chapter/code       [:vector :code]
   :project.chapter/story      [:vector :story]
   ; chapter in a project containing something new to learn and a story
   :project-chapter            [:map {:closed true}
                                :project.chapter/id
                                :project.chapter/name
                                :project.chapter/notes
                                [:project.chapter/wiki-ref {:optional true}]
                                :project.chapter/code]


   :wiki/id                    :uuid
   :wiki/tags                  [:vector keyword?]
   :wiki/title                 :string
   :wiki/content               :string
   ; short explanation on a programming topic for a certain language
   :wiki                       [:map {:closed true}
                                [:xt/id :wiki/id]
                                :wiki/tags
                                :wiki/title
                                :wiki/content]})

(def malli-opts {:registry (malr/composite-registry malc/default-registry schema)})
