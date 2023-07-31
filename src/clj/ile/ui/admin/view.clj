(ns ile.ui.admin.view
  (:require [ile.dictonary.translations :as tr]
            [ile.util :as util]))


(defn users-page [])

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

(defn- mission-content [{:mission.content/keys [mode result hidden-html hidden-css
                                               wrong-blocks]}
                        difficulty]
  [:<>
   [:label "Modus"]
   [:select {:name (str "mission-content_mode-" difficulty)}
    [:option {:value    "html"
              :selected (when (= mode :html) "selected")} "HTML"]
    [:option {:value    "css"
              :selected (when (= mode :css) "selected")} "CSS"]]
   [:label "Code"]
   [:p "Für jeden Code Snippet eine neue Zeile.\nReihenfolge = korrekte Lösung."]
   [:textarea {:value (vector-to-multiline result)
               :name  (str "mission-content_" difficulty)}]

   [:label "Falsche Blöcke"]
   [:p "Für jeden Block eine neue Zeile."]
   [:textarea {:value (vector-to-multiline wrong-blocks)
               :name  (str "mission-content_" difficulty "-wrong")}]
   [:label "Verstecktes HTML"]
   [:p "Valides HTML. $$placeholder$$ setzen wo Eingaben eingefügt werden, wenn HTML Aufgabe ist."]
   [:textarea {:value hidden-html
               :name  (str "mission-content_hidden-html-" difficulty)}]

   [:label "Verstecktes CSS"]
   [:p "Valides CSS. $$placeholder$$ setzen wo Eingaben eingefügt werden, wenn CSS Aufgabe ist."]
   [:textarea {:value hidden-css
               :name  (str "mission-content_hidden-css-" difficulty)}]]
  )

(defn edit-mission-page
  [{:mission/keys [name _world step content
                   story-before story-after] :as template
    :xt/keys [id]}]
  [:<>
   [:nav
    [:a.button {:href "/admin/stories"} "Zurück / Abrechen"]]
   [:main#template-editor
    [:h1
     (if (nil? template)
       "Neue Mission anlegen"
       (str "Mission \"" name "\" bearbeiten"))]
    [:form.form-tile
     {:action (str "/admin/story/" (or id "new"))
      :method :post}
     (util/hidden-anti-forgery-field)
     [:label "Name"]
     [:input {:value    name
              :required true
              :name     "mission_name"}]
     #_[:label "Welt"]
     ; make html css default for now
     #_[:input {:value    world
                :required true
                :name     "mission_world"}]
     [:label "Schritt"]
     [:input {:value    step
              :required true
              :type     :number
              :min      1
              :step     1
              :max      999
              :name     "mission_step"}]
     [:label "Story vorher"]
     [:p "Für jede Nachricht eine neue Zeile"]

     [:textarea {:value (->
                          story-before
                          vector-to-multiline)
                 :name  "mission_story-before"}]

     [:label "Story danach"]
     [:p "Für jede Nachricht eine neue Zeile"]
     [:textarea {:value (->
                          story-after
                          vector-to-multiline)
                 :name  "mission_story-after"}]

     [:h2 "Inhalte"]
     (mission-content (content-for-difficulty content :easy) "easy")

     [:h3 "Mittel"]
     (mission-content (content-for-difficulty content :medium) "medium")

     [:h3 "Schwer"]
     (mission-content (content-for-difficulty content :hard) "hard")

     [:button {:type :submit} "Speichern"]]]])

(defn story-page [missions]
  [:<>
   [:nav
    [:a.button {:href "/admin"} "Zurück"]
    [:a.button {:href "/admin/story/new"} "Neue Mission"]]
   [:main#story-page
    [:h1 "Stories"]
    (for [[world-key world-missions] missions]
      [:<>
       [:h2 (str "Welt: " (get-world-name world-key))]
       (for [{:mission/keys [_world step name]
              :xt/keys      [id]} world-missions]
         [:a.button.stacked
          {:href (str "/admin/story/" id)}
          [:h5 (str "Level " step)]
          [:h3 name]])])]])

(defn admin-page []
  [:<>
   [:nav
    [:a.button {:href "/de/"} "Zu Startseite"]]
   [:main#admin-page
    [:h1 "Admin Area"]
    [:a.button {:href "/admin/users"} "Users"]
    [:a.button {:href "/admin/templates"} "Templates"]
    [:a.button {:href "/admin/stories"} "Story"]]])