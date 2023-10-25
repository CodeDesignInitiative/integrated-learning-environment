(ns ile.routes.home
  (:require [buddy.auth :refer [authenticated?]]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as string]
            [muuntaja.middleware :as muuntaja-middleware]

            [ile.components.app :as app]
            [ile.courses.html-website :as html-website]
            [ile.projects.core :as projects]
            [ile.views.chat-screen :as chat-screen]
            [ring.util.response :as response]
            [ile.layout :as layout]
            [ile.middleware :as middleware]

            [ile.ui.projects.core :as projects-page]
            [ile.ui.auth.core :as auth-page]
            [ile.ui.admin.core :as admin-page]
            [ile.story.core :as story-page]

            [ile.ui.start.core :as start-page]
            [ile.ui.legal.core :as legal-page]))





(defn home [request]
  (if-not (authenticated? request)
    (response/redirect "/login")
    (layout/render-page

      [:h1 "Hello " (get-in request [:session :identity])])))



(def grid-html "\n<h1>Meine Website</h1>\n\n<main>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n        <a href=\"https://code-editor.digital\">code+design LMS</a>\n    </article>\n    <article class=\"highlight\">\n        <h2>CSS Tips und Tricks</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n        <a href=\"https://css-tricks.com/\">css-tricks.com</a>\n    </article>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n    <article class=\"highlight\">\n        <h2>CSS Tips und Tricks</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n    <article>\n        <h2>Neue HTML Kurse</h2>\n        <p>Lorem ipsum dolet sunt.</p>\n    </article>\n</main>")
(def grid-css "body {\n    background-color: #f5f5f5;\n    font-family: Helvetica, sans-serif;\n}\n\nmain {\n    /* Zeige alles, was im Main-Tag ist in einem Raster an */\n    display: grid;\n    /* zwei Spalten */\n    grid-template-columns: 1fr 1fr;\n    /* 12px Abstand zwischen Spalten & Zeilen */\n    gap: 12px;\n}\n\narticle {\n    border: 1px solid #ddd;\n    border-radius: 12px;\n    padding: 0 12px;\n    min-width: 260px;\n    min-height: 140px;\n}\n\na {\n    color: #42d5ac;\n    text-decoration: none;\n    font-weight: bold;\n    transition-property: color;\n    transition-duration: .3s;\n    transition-timing-function: ease-in-out;\n}\n\na:hover {\n    text-decoration: underline;\n    color: #4c42d5;\n    transition-property: color;\n    transition-duration: .3s;\n    transition-timing-function: ease-in-out;\n}\n\n.highlight {\n    background-color: #555;\n    color: white\n}")

(def list-de-html
  "
<h1>Mein Steckbrief</h1>
<ul>
  <li>Name: Luca Mensch</li>
  <li>Alter: Unbekannt</li>
  <li>Hobbies: Schwimmen, tanzen, gaming</li>
</ul>
  ")

(def list-ru-html
  "
<h1>Мой профиль пользователя</h1>
<ul>
  <li>Имя: Лука Человек</li>
  <li>Возраст: Неизвестно</li>
  <li>Хобби: плавание, танцы, игры</li>
</ul>
  ")

(def list-css
  "
body {
  background-color: #EFECEC;
  font-family: sans-serif;
  padding: 12px;
}
  ")

(def list-color-de-html
  "
<h1>Mein Steckbrief</h1>

<img src=\"https://images.unsplash.com/photo-1600077106724-946750eeaf3c?fit=crop&w=2670&q=80\">

<ul>
  <li>Name: Luca Mensch</li>
  <li>Alter: Unbekannt</li>
  <li>Hobbies: Schwimmen, tanzen, gaming</li>
</ul>
  ")

(def list-color-ru-html
  "
<h1>Мой профиль пользователя</h1>

<img src=\"https://images.unsplash.com/photo-1600077106724-946750eeaf3c?fit=crop&w=2670&q=80\">

<ul>
  <li>Имя: Лука Человек</li>
  <li>Возраст: Неизвестно</li>
  <li>Хобби: плавание, танцы, игры</li>
</ul>
  ")

(def list-color-css
  "
body {
  background-color: red;
}

li {
  background-color: blue;
  color: green;
}



img {
  width: 200px;
}
  ")

(defn get-template-code [template]
  (condp = template
    "list-color-de" {:html list-color-de-html
                     :css  list-color-css}
    "list-color-ru" {:html list-color-ru-html
                     :css  list-color-css}
    "list-de" {:html list-de-html
               :css  list-css}
    "list-ru" {:html list-ru-html
               :css  list-css}
    "grid" {:html grid-html
            :css  grid-css}
    "blog" {:html (string/replace html-website/html-base #"\$\$placeholder\$\$"
                                  (string/replace html-website/html-1-snippet #"\$\$placeholder\$\$" html-website/html-1-line))
            :css  html-website/css-website}))

(defn new-project [request]
  (let [template (get-in request [:form-params "template"])
        project-name (get-in request [:form-params "project-name"])
        user-email (-> (get-in request [:session :identity]) name)
        template-code (get-template-code template)
        project-id (random-uuid)]
    (do
      (projects/create-user-project (merge {:xt/id project-id}
                                              #:user.project{:name  project-name
                                                             :owner user-email
                                                             :css   (if template (:css template-code) "")
                                                             :html  (if template (:html template-code) "")}))
      (println "\n\nRedirect...\n\n")
      (response/redirect (str "/projekt?id=" project-id)))))

(defn project-editor [request]
  (let [project-id (get-in request [:query-params "id"])
        project (projects/find-user-project (parse-uuid project-id))]
    (ile.views.editor-screen/editor-screen
      {:html {:code/line (:user.project/html project)}
       :css  {:code/base (:user.project/css project)}}
      nil nil)
    ))

(defn redirect-new-project [request]
  (let [template (get-in request [:form-params "template"])
        project-name (get-in request [:form-params "name"])
        user-email (-> (get-in request [:session :identity]) name)
        project-id (random-uuid)]

    (projects/create-user-project (merge {:xt/id project-id}
                                            #:user.project{:name  project-name
                                                           :owner user-email
                                                           :css   (or (:css template) "")
                                                           :html  (or (:html template) "")}))
    (println "\n\nRedirect...\n\n")
    (response/redirect (str "/projekt?id=" project-id))
    ))


(defn chat-screen [requset]
  (chat-screen/chat-screen html-website/start-chat-with-edna 1 "website1" 1))


(defn redirect-start-lang-de [r]
  (response/redirect "/de/"))

(def public-routes
  auth-page/routes)

(def private-routes
  ["" {:middleware [middleware/wrap-unauthorized-login-redirect
                    middleware/wrap-csrf
                    middleware/wrap-render-rum
                    middleware/wrap-formats]}

   ["/" {:get redirect-start-lang-de}]
   start-page/routes
   projects-page/routes
   admin-page/routes
   story-page/routes
   ["/datenschutz" {:get legal-page/privacy-statement
                    }]
   ["/chat" {:get chat-screen}]
   ["/wiki" {:get app/wiki}]
   ["/auftraege" {:get app/jobs}]
   ["/auftrag" {:get app/job-step}]
   #_["/projekte"
      ["" {:get  editor-screen/editor-project-selection-screen
           #_projects-page/projects-page
           :post new-project}]
      ["/neu" {:post new-project
               :get  redirect-new-project}]]
   ["/editor" {:get app/editor}]
   ["/settings" {:get app/settings}]])

(def api-routes
  ["/api" {:middleware [muuntaja-middleware/wrap-format-response]}
   story-page/api-routes])

(def htmx-routes
  ["/htmx" {:middleware [middleware/wrap-render-htmx
                         middleware/wrap-unauthorized-login-redirect
                         middleware/wrap-csrf
                         middleware/wrap-render-rum
                         middleware/wrap-formats]}
   admin-page/htmx-routes])