(ns ile.layout
  (:require [cheshire.core :as cheshire]
            [ring.util.http-response :refer [content-type ok]]
            [ring.util.response]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [rum.core :as rum]))


(defn html-page-wrapper [page]
  [:html.full-height
   [:head

    [:title "play+learn"]

    ; favicon
    [:link {:rel "apple-touch-icon" :sizes "180x180" :href "/apple-touch-icon.png"}]
    [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/favicon-32x32.png"}]
    [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/favicon-16x16.png"}]
    [:link {:rel "manifest" :href "/site.webmanifest"}]
    [:link {:rel "mask-icon" :href "/safari-pinned-tab.svg" :color "#5bbad5"}]
    [:meta {:name "msapplication-TileColor" :content "#da532c"}]
    [:meta {:name "theme-color" :content "#ffffff"}]

    ; custom css
    [:link {:rel  :stylesheet
            :href "/css/base.css?v=5"}]
    [:link {:rel  :stylesheet
            :href "/css/components.css?v=3"}]
    [:link {:rel  :stylesheet
            :href "/css/color.css?v=2"}]
    [:link {:rel  :stylesheet
            :href "/css/font.css?v=3"}]
    [:link {:rel  :stylesheet
            :href "/css/layout.css?v=2"}]

    ; js
    [:script {:src   "/js/htmx.min.js"
              :defer "defer"}]
    [:script {:src   "/js/story-editor.js"
              :defer "defer"}]]

   [:body
    (when (bound? #'*anti-forgery-token*)
      {:hx-headers (cheshire/generate-string
                     {:x-csrf-token *anti-forgery-token*})
       #_#_:hx-boost "true"})
    page
    ]])



(defn render-page
  "renders the page wrapped in the base HTML"
  [hiccup-content]
  (content-type
    (ok (str "<!DOCTYPE html>\n"
             (rum/render-static-markup
               (html-page-wrapper hiccup-content))))
    "text/html; charset=utf-8"))

(defn render-htmx
  [hiccup-content]
  (content-type
    (ok (str "<!DOCTYPE html>\n"
             (rum/render-static-markup hiccup-content)))
    "text/html; charset=utf-8"))


(defn error-page
  "error-details should be a map containing the following keys:
   :status - error status
   :title - error title (optional)

   returns a response map with the error page as the body
   and the status specified by the status key"
  [{:keys [status title]}]
  (render-page
    [:main#error-page
     [:nav
      [:a.button {:href "/"} "Zurück zum Start"]
      [:a.button {:href "/login"} "Anmelden"]
      [:a.button {:href "/logout"} "Abmelden"]]
     [:div
      [:h1 (str title)]
      [:h2 (str "Fehlercode: " status)]
      [:p "Aber alles kein Problem. Am besten gehst du zurück auf die Startseite: "
       [:a.button {:href "/"} "Zurück zum Start"]]
      [:img {:src "/img/puppies.gif"}]]]))