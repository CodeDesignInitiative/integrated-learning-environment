(ns cd.ile.ui
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [com.biffweb :as biff]
            [ring.middleware.anti-forgery :as csrf]))

(defn css-path []
  (if-some [f (io/file (io/resource "public/css/main.css"))]
    (str "/css/main.css?t=" (.lastModified f))
    "/css/main.css"))

(defn base [opts & body]
  (apply
    biff/base-html
    (-> opts
        (merge #:base{:title       "ILE project"
                      :lang        "en-US"
                      :icon        "/img/persons/test.jpg"
                      :description "Spielend Programmieren lernen"
                      :image       "https://clojure.org/images/clojure-logo-120b.png"})
        (update :base/head (fn [head]
                             (concat [[:link {:rel "stylesheet" :href (css-path)}]
                                      [:link {:rel "stylesheet" :href "/css/base.css?v=1"}]
                                      [:link {:rel "stylesheet" :href "//unpkg.com/@highlightjs/cdn-assets@11.7.0/styles/atom-one-dark.min.css"}]
                                      ;[:script {:src "/js/editor.js"}]
                                      [:script {:src "https://unpkg.com/htmx.org@1.8.4"}]
                                      [:script {:src "https://unpkg.com/hyperscript.org@0.9.3"}]
                                      [:script {:src "//unpkg.com/@highlightjs/cdn-assets@11.7.0/highlight.min.js"}]
                                      [:script {:src "/js/refresh.js"}]]
                                     head))))
    [:<> body
     [:script {:src "/js/src-min-noconflict/ace.js"
               :type "text/javascript"
               :charset "utf-9"}]
     [:script {:src "/js/editor.js"}]]))

(defn page [opts & body]
  (base opts
        [:div.h-full
         (when (bound? #'csrf/*anti-forgery-token*)
           {:hx-headers (cheshire/generate-string
                          {:x-csrf-token csrf/*anti-forgery-token*})})
         [:header.bg-zinc-800.py-2.text-white.font-mono.flex.items-center.justify-center
          [:a {:href "/app"}
           [:div
            "code "
            [:span.text-red-700 "//"]
            " editor"]]]
         body]))
