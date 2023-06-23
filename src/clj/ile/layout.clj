(ns ile.layout
  (:require [cheshire.core :as cheshire]
            [ring.util.http-response :refer [content-type ok]]
            [ring.util.response]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [rum.core :as rum]))

(defn error-page
  "error-details should be a map containing the following keys:
   :status - error status
   :title - error title (optional)

   returns a response map with the error page as the body
   and the status specified by the status key"
  [error-details]
  :status (:status error-details)
  :headers {"Content-Type" "text/html; charset=utf-8"}
  :body [:div (str error-details)])


(defn page [page]
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
            :href "/css/base.css?v=3"}]
    [:link {:rel  :stylesheet
            :href "/css/components.css?v=3"}]
    [:link {:rel  :stylesheet
            :href "/css/color.css?v=2"}]
    [:link {:rel  :stylesheet
            :href "/css/font.css?v=2"}]
    [:link {:rel  :stylesheet
            :href "/css/layout.css?v=2"}]
    ]
   [:body
    page

    [:#legal
     [:a.link {:href "https://code.design/impressum"}
      "Impressum"]
     [:a.link {:href "/datenschutz"}
      "Datenschutz"]]]])


(defn render-page
  "renders the page wrapped in the base HTML"
  [p]
  (content-type
    (ok (str "<!DOCTYPE html>\n"
             (rum/render-static-markup
               (page p))))
    "text/html; charset=utf-8"))