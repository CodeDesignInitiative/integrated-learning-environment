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

    [:link {:rel  :stylesheet
            :href "/css/base.css?v=2"}]]
   [:body


    page
    [:script {:src "/js/chat.js"}]
    ;[:script {:src "/js/src-min-noconflict/ace.js"
    ;          :type "text/javascript"
    ;          :charset "utf-9"}]
    ;[:script {:src "/js/editor.js"}]
    ]])


(defn render-page
  "renders the page wrapped in the base HTML"
  [p]
  (content-type
    (ok (str "<!DOCTYPE html>\n"
             (rum/render-static-markup
               (page p))))
    "text/html; charset=utf-8"))