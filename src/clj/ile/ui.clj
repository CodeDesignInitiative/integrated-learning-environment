(ns ile.ui
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [ring.middleware.anti-forgery :as csrf]))

(defn css-path []
  (if-some [f (io/file (io/resource "public/css/main.css"))]
    (str "/css/main.css?t=" (.lastModified f))
    "/css/main.css"))

(defn base-html
  [{:base/keys [title
                description
                lang
                image
                icon
                url
                canonical
                font-families
                head]}
   & contents]
  [:html
   {:lang lang
    :style {:min-height "100%"
            :height "auto"}}
   [:head
    [:title title]
    [:meta {:name "description" :content description}]
    [:meta {:content title :property "og:title"}]
    [:meta {:content description :property "og:description"}]
    (when image
      [:<>
       [:meta {:content image :property "og:image"}]
       [:meta {:content "summary_large_image" :name "twitter:card"}]])
    (when-some [url (or url canonical)]
      [:meta {:content url :property "og:url"}])
    (when-some [url (or canonical url)]
      [:link {:ref "canonical" :href url}])
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    (when icon
      [:link {:rel "icon"
              :type "image/png"
              :sizes "16x16"
              :href icon}])
    [:meta {:charset "utf-8"}]
    (when (not-empty font-families)
      [:<>
       [:link {:href "https://fonts.googleapis.com", :rel "preconnect"}]
       [:link {:crossorigin "crossorigin",
               :href "https://fonts.gstatic.com",
               :rel "preconnect"}]])
    (into [:<>] head)]
   [:body
    {:style {:position "absolute"
             :width "100%"
             :min-height "100%"
             :display "flex"
             :flex-direction "column"}}
    contents]])

(defn base [opts & body]
  (apply
    base-html
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
                                      #_[:script {:src "/js/refresh.js"}]]
                                     head))))
    body))

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
         body
         [:script {:src "/js/chat.js"}]]))
