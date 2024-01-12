(ns ile.ui.admin.view
  (:require
    [clojure.string :as string]
    [ile.dictonary.translations :as tr]
    [ile.core.util :as util]
    [ile.user.core :as user])
  (:import (java.io File)))

(defn- user-row [{:user/keys [roles]
                  :xt/keys   [id]}
                 is-admin?
                 current-user]
  [:div.tile.row.gap.align-center
   [:p "Username: " [:b id]]
   [:div.row
    (when (some #{:admin} roles) [:span.badge "Admin"])
    (when (some #{:teacher} roles) [:span.badge "Teacher"])]
   (when (and is-admin?
              (not= id (:xt/id current-user)))
     (if (some #{:teacher} roles)
       [:button {:hx-post (str "/htmx/admin/make-student/" id)
                 :hx-swap "none"}
        "Make Student"]
       [:button {:hx-post (str "/htmx/admin/make-teacher/" id)
                 :hx-swap "none"}
        "Make Teacher"]))
   #_[:p "Email: " email]])

(defn users-page [is-admin? current-user]
  (let [users (user/find-all-users)]
    [:<>
     [:nav
      [:a.button {:href "/admin"} "Zurück"]]
     [:main#users.p3
      (map #(user-row % is-admin? current-user) users)]]
    )
  )

(defn- template-row-item
  [{:template/keys [name code visible name-translations]
    :xt/keys       [id]}]
  [:a.button {:href (str "/admin/template/" id)}
   name])

(defn templates-page [templates]
  [:<>
   [:nav
    [:a.button {:href "/admin"} "Zurück"]
    [:a.button {:href "/admin/template/new"} "Neues Template"]]
   [:main#templates-page
    [:h1 "All templates"]
    (map template-row-item templates)]])

(defn template-edit-page
  [{:template/keys [name code visible? name-translations] :as template
    :xt/keys       [id]}]
  [:<>
   [:nav
    [:a.button {:href "/admin/templates"} "Zurück / Abrechen"]]
   [:main#template-editor
    [:h1
     (if (nil? template)
       "Neues Template anlegen"
       (str "Template \"" name "\" bearbeiten"))]
    [:form.form-tile
     {:action (str "/admin/template/" (or id "new"))
      :method :post}
     (util/hidden-anti-forgery-field)
     [:label "Name"]
     [:input {:value    name
              :required true
              :name     "template_name"}]
     [:label "Sichtbar?"]
     [:input {:type    :checkbox
              :checked (or visible? false)
              :name    "visible?"}]
     [:label "HTML Code"]
     [:textarea {:value (:html code)
                 :name  "html_code"}]
     [:label "CSS Code"]
     [:textarea {:value (:css code)
                 :name  "css_code"}]
     [:label "JS Code"]
     [:textarea {:value (:js code)
                 :name  "js_code"}]
     [:button {:type :submit} "Speichern"]]]])

(defn get-world-name [world-key]
  (get
    {:html-css "HTML & CSS"}
    world-key))

(defn vector-to-multiline [data-vector]
  (str
    (reduce
      (fn [res entry]
        (str res entry "\n"))
      ""
      data-vector)))

(defn content-for-difficulty [content given-difficulty]
  (first (filter (fn [{:mission.content/keys [difficulty] :as c}]
                   (= difficulty given-difficulty)) content)))

(defn- mission-content
  [{:mission.content/keys [mode result hidden-html hidden-css
                           input-type
                           wrong-blocks hint explanation]}
   difficulty]
  [:div.form-cols.hidden
   {:id (str "content-" difficulty)}
   [:label "Modus"]
   [:select {:name (str "mission-content_mode-" difficulty)}
    [:option {:value    "html"
              :selected (when (= mode :html) "selected")} "HTML"]
    [:option {:value    "css"
              :selected (when (= mode :css) "selected")} "CSS"]]


   [:label "Eingabe Typ"]
   [:select {:name (str "mission-content_input-type-" difficulty)}
    [:option {:value    "block"
              :selected (when (= input-type :block) "selected")} "Block Editor"]
    [:option {:value    "text"
              :selected (when (= input-type :text) "selected")} "Text Editor"]]

   [:div
    [:label "Erklärung"]
    [:p "Erklärung für die Mission im Editor"]]
   [:textarea {:value explanation
               :name  (str "mission-content_explanation-" difficulty)}]

   [:div
    [:label "Hinweis"]
    [:p "Hinweis, wenn falsche Eingabe getätigt wurde."]]
   [:textarea {:value hint
               :name  (str "mission-content_hint-" difficulty)}]

   [:div
    [:label "Code"]
    [:p "Für jeden Code Snippet eine neue Zeile.\nReihenfolge = korrekte Lösung."]]
   [:textarea {:value (vector-to-multiline result)
               :name  (str "mission-content_" difficulty)}]

   [:div
    [:label "Falsche Blöcke"]
    [:p "Für jeden Block eine neue Zeile."]]
   [:textarea {:value (vector-to-multiline wrong-blocks)
               :name  (str "mission-content_" difficulty "-wrong")}]

   [:div
    [:label "Verstecktes HTML"]
    [:p "Valides HTML. $$placeholder$$ setzen wo Eingaben eingefügt werden, wenn HTML Aufgabe ist."]]
   [:textarea {:value hidden-html
               :name  (str "mission-content_hidden-html-" difficulty)}]

   [:div
    [:label "Verstecktes CSS"]
    [:p "Valides CSS. $$placeholder$$ setzen wo Eingaben eingefügt werden, wenn CSS Aufgabe ist."]]
   [:textarea {:value hidden-css
               :name  (str "mission-content_hidden-css-" difficulty)}]]
  )

(defn edit-mission-page
  [{:mission/keys [name _world step content
                   story-before story-after] :as template
    :xt/keys [id]}
   lang
   [error]]
  [:form
   {:action (str "/admin/story/" (or id "new") "/" (clojure.core/name lang))
    :method :post}
   [:nav
    [:a.button {:href "/admin/stories"} "Zurück"]
    [:h3
     (if (nil? template)
       "Neue Mission anlegen"
       (str "Mission \"" (lang name) "\" bearbeiten"))]
    [:button {:type :submit} "Speichern"]]
   [:main#story-editor
    (util/hidden-anti-forgery-field)
    (when error
      [:p error])
    [:div#tabs
     [:button#general-btn.active
      {:on-click "(e) => open_tab(e, 'general')"}
      "Mission"]
     [:button#story-btn
      {:on-click "(e) => open_tab(e, 'story')"}
      "Story"]
     [:label
      "Content"]
     [:button#content-easy-btn
      {:on-click "open_tab('content-easy')"}
      "Einfach"]
     [:button#content-medium-btn
      {:on-click "open_tab('content-medium')"}
      "Mittel"]
     [:button#content-hard-btn
      {:on-click "open_tab('content-hard')"}
      "Schwer"]]
    [:div#general.form-cols
     [:label "Schritt"]
     [:input {:value    step
              :required true
              :type     :number
              :min      1
              :step     1
              :max      999
              :name     "mission_step"}]
     [:label "Sprache"]
     [:#language-select
      [:a.button
       {:href  "./de"
        :class (when (= lang :de) "active")}
       "Deutsch"]
      [:a.button
       {:href  "./ru"
        :class (when (= lang :ru) "active")}
       "Russisch"]]

     [:label "Name"]
     [:input {:value    (lang name)
              :required true
              :name     "mission_name"}]]

    [:#story.hidden
     [:div.form-cols
      [:div
       [:label "Story vorher"]
       [:p "Für jede Nachricht eine neue Zeile"]]

      [:textarea {:value (->
                           story-before
                           lang
                           vector-to-multiline)
                  :name  "mission_story-before"}]

      [:div
       [:label "Story danach"]
       [:p "Für jede Nachricht eine neue Zeile"]]
      [:textarea {:value (->
                           story-after
                           lang
                           vector-to-multiline)
                  :name  "mission_story-after"}]]]

    (mission-content (content-for-difficulty (lang content) :easy) "easy")

    (mission-content (content-for-difficulty (lang content) :medium) "medium")

    (mission-content (content-for-difficulty (lang content) :hard) "hard")

    ]
   #_[:script {:src "/js/story-editor.js"}]
   ])

(defn story-page [missions]
  [:<>
   [:nav
    [:a.button {:href "/admin"} "Zurück"]
    [:a.button {:href "/admin/story/new/de"} "Neue Mission"]]
   [:main#story-page
    [:h1 "Stories"]
    (for [[world-key world-missions] missions]
      [:<>
       [:h2 (str "Welt: " (get-world-name world-key))]
       (for [{:mission/keys [_world step name]
              :xt/keys      [id]} world-missions]
         [:a.button.stacked
          {:href (str "/admin/story/" id "/de")}
          [:h5 (str "Level " step)]
          [:h3 (:de name)]])])]])

(defn images-page [images]
  [:<>
   [:nav
    [:a.button {:href "/admin"} "Zurück"]]
   [:main#images-page
    [:h1 "Images"]
    [:form.row {:action  "/admin/images"
                :method  :post
                :enctype "multipart/form-data"}
     (util/hidden-anti-forgery-field)
     [:label "File"]
     [:input {:type   :file
              :name   :image-asset
              :accept "image/png, image/jpeg"}]
     [:button {:type :submit} "Upload file"]]
    [:div
     (map
       (fn [^File f]
         [:figure

          [:img {:src (str "/" (.getName f))}]
          [:figcaption (.getName f)]])
       images)
     ]]])

(defn admin-page []
  [:<>
   [:nav
    [:a.button {:href "/de/"} "Zu Startseite"]]
   [:main#admin-page
    [:h1 "Admin Area"]
    [:a.button {:href "/admin/users"} "Users"]
    [:a.button {:href "/admin/templates"} "Templates"]
    [:a.button {:href "/admin/learning-tracks"} "Learning Tracks"]
    [:a.button {:href "/admin/stories"} "Story (to be deprecated)"]]])

(defn learning-tracks-detail-page [{:learning-track/keys [name description language visible?]}
                                   learning-track-id
                                   learning-track-tasks]
  [:<>
   [:nav
    [:a.button {:href "/admin/learning-tracks"} "<- Back"]
    [:a.button {:href (str "/admin/learning-track/edit/" learning-track-id)} "Edit"]]
   [:main#admin-page
    [:section
     [:h1 name]
     [:p description]
     [:p "Language: " (clojure.core/name language)]
     [:p "Visible: " visible?]]
    [:a.button {:href (str "/admin/learning-track/" learning-track-id "/tasks/new")}
     "New Task for Learning Track"]
    [:section
     [:h2 "Tasks"]
     [:h3 "Active Tasks"]
     [:ul
      (map (fn [{:learning-track-task/keys [name step block-mode? editor-modes]
                 :xt/keys                  [id]}]
             [:li
              [:strong name]
              [:p "Step: " step]
              [:p "Block mode: " (if block-mode? block-mode? false)]
              [:p "Editor modes: " (str editor-modes)]
              [:a {:href (str "/admin/learning-track/" learning-track-id "/task/" id)} "Edit"]])
           (get learning-track-tasks true))]
     [:h2 "Tasks"]
     [:h3 "Deactivated Tasks"]
     [:ul
      (map (fn [{:learning-track-task/keys [name step block-mode? editor-modes]
                 :xt/keys                  [id]}]
             [:li
              [:strong name]
              [:p "Step: " step]
              [:p "Block mode: " (if block-mode? block-mode? false)]
              [:p "Editor modes: " (str editor-modes)]
              [:a {:href (str "/admin/learning-track/" learning-track-id "/task/" id)} "Edit"]])
           (concat (get learning-track-tasks nil) (get learning-track-tasks false)))]]]])


(defn new-learning-track-task-page [{:learning-track/keys [name description language]
                                     :xt/keys             [id]}
                                    & [learning-track-task]]
  [:<>
   [:nav
    [:a.button {:href (str "/admin/learning-track/" id)} "Back"]]
   [:main.p3
    (if learning-track-task
      [:h1 "Edit Task"]
      [:h1 "New Task"])
    [:p "Learning Track: " [:strong name]]
    [:p "Language: " [:strong (string/upper-case (clojure.core/name language))]]
    [:p "Description: " [:strong description]]

    [:form.grid.container {:action (str "/admin/learning-track/" id
                                        (if learning-track-task
                                          (str "/task/" (:xt/id learning-track-task))
                                          "/tasks/new"))
                           :method :post}

     (util/hidden-anti-forgery-field)

     [:label {:form "learning-track-task/name"} "Name*"]
     [:input {:name        "learning-track-task/name"
              :required    true
              :value       (:learning-track-task/name learning-track-task)
              :placeholder "e.g. 'Hallo Welt'"}]

     [:label {:form "learning-track-task/step"} "Step*"]
     [:input {:name        "learning-track-task/step"
              :placeholder "1 - n"
              :type        :number
              :value       (:learning-track-task/step learning-track-task)
              :required    true
              :step        1}]

     [:div
      [:label {:form "learning-track-task/block-mode?"} "Block Mode?"]
      [:p "Defines whether this task is solved by typing (default) or using draggable blocks (= block mode)."]]
     [:div [:input {:name    "learning-track-task/block-mode?"
                    :checked (:learning-track-task/block-mode? learning-track-task)
                    :type    :checkbox}]]

     [:div
      [:label {:form "learning-track-task/active?"} "Active?"]]
     [:div [:input {:name    "learning-track-task/active?"
                    :checked (:learning-track-task/active? learning-track-task)
                    :type    :checkbox}]]

     [:div
      [:label {:form "learning-track-task/editor-modes"} "Editor Modes*"]
      [:p "Modes of the editor which shall be available for this task."]
      [:p [:strong "Important: "] "At least one needs to be selected."]]
     [:div.col.gap.align-start
      [:label.row.gap
       [:input {:name    "learning-track-task/editor-modes"
                :value   :html
                :checked (boolean (some #{:html} (:learning-track-task/editor-modes learning-track-task)))
                :type    :checkbox}]
       "HTML"]
      [:label.row.gap
       [:input {:name    "learning-track-task/editor-modes"
                :value   :css
                :checked (boolean (some #{:css} (:learning-track-task/editor-modes learning-track-task)))
                :type    :checkbox}]
       "CSS"]
      [:label.row.gap
       [:input {:name    "learning-track-task/editor-modes"
                :value   :js
                :checked (boolean (some #{:js} (:learning-track-task/editor-modes learning-track-task)))
                :type    :checkbox}]
       "JS"]]

     [:label {:form "learning-track-task/explanation"} "Explanation*"]
     [:textarea {:name     "learning-track-task/explanation"
                 :value    (:learning-track-task/explanation learning-track-task)
                 :required true}]

     [:div
      [:label {:form "learning-track-task/solution"} "Solution*"]
      [:p "Important for Block Mode: Each line represents a new block. The line order equals the correct order of the solution"]]
     [:textarea {:name     "learning-track-task/solution"
                 :value    (:learning-track-task/solution learning-track-task)
                 :required true}]

     [:div
      [:label {:form "learning-track-task/hint"} "Hint"]
      [:p "A hint shown after the user clicks on the ?-icon."]]
     [:textarea {:name  "learning-track-task/hint"
                 :value (:learning-track-task/hint learning-track-task)}]

     [:div
      [:label {:form "learning-track-task/messages-before"} "Messages before"]
      [:p "For chat mode add a new line for each new messages. Add images by starting a line with [img] followed by the image path, e.g. `[img]banana.jpg`."]
      [:p "Use markdown for formatting."]]
     [:textarea {:name  "learning-track-task/messages-before"
                 :value (:learning-track-task/messages-before learning-track-task)}]

     [:div
      [:label {:form "learning-track-task/messages-after"} "Messages after"]
      [:p "For chat mode add a new line for each new messages. Add images by starting a line with [img] followed by the image path, e.g. `[img]banana.jpg`."]
      [:p "Use markdown for formatting."]]
     [:textarea {:name  "learning-track-task/messages-after"
                 :value (:learning-track-task/messages-after learning-track-task)}]

     [:label {:form "learning-track-task/visible-html"} "Visible HTML"]
     [:textarea {:name  "learning-track-task/visible-html"
                 :value (:learning-track-task/visible-html learning-track-task)}]

     [:label {:form "learning-track-task/hidden-html"} "Hidden HTML"]
     [:textarea {:name  "learning-track-task/hidden-html"
                 :value (:learning-track-task/hidden-html learning-track-task)}]

     [:label {:form "learning-track-task/visible-css"} "Visible CSS"]
     [:textarea {:name  "learning-track-task/visible-css"
                 :value (:learning-track-task/visible-css learning-track-task)}]

     [:label {:form "learning-track-task/hidden-css"} "Hidden CSS"]
     [:textarea {:name  "learning-track-task/hidden-css"
                 :value (:learning-track-task/hidden-css learning-track-task)}]

     [:label {:form "learning-track-task/visible-js"} "Visible JS"]
     [:textarea {:name  "learning-track-task/visible-js"
                 :value (:learning-track-task/visible-js learning-track-task)}]

     [:label {:form "learning-track-task/hidden-js"} "Hidden JS"]
     [:textarea {:name  "learning-track-task/hidden-js"
                 :value (:learning-track-task/hidden-js learning-track-task)}]

     [:div
      [:label {:form "learning-track-task/wrong-blocks"} "Wrong Blocks"]
      [:p "Only relevant for tasks in block mode."]
      [:p "Each line represents a new block. The line order does not matter and gets shuffeld with the solution blocks."]]
     [:textarea {:name  "learning-track-task/wrong-blocks"
                 :value (:learning-track-task/wrong-blocks learning-track-task)}]

     [:div]
     [:button {:type :submit}
      (if learning-track-task
        "Update Task"
        "Create Task")]]]])