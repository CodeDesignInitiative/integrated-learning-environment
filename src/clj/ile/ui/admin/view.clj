(ns ile.ui.admin.view
  (:require
    [ile.dictonary.translations :as tr]
    [ile.util :as util]
    [ile.user.core :as user]))

(defn- user-row [{:user/keys [email]
                  :xt/keys   [id]}]
  [:div.tile
   [:p "Username: " [:b id]]
   [:p "Email: " email]])

(defn users-page []
  (let [users (user/find-all-users)]
    [:<>
     [:nav
      [:a.button {:href "/admin"} "Zurück"]]
     [:main#users.p3
      (map user-row users)]]
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
   [:script {:src "/js/story-editor.js"}]
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

(defn admin-page []
  [:<>
   [:nav
    [:a.button {:href "/de/"} "Zu Startseite"]]
   [:main#admin-page
    [:h1 "Admin Area"]
    [:a.button {:href "/admin/users"} "Users"]
    [:a.button {:href "/admin/templates"} "Templates"]
    [:a.button {:href "/admin/stories"} "Story"]]])