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
                      :icon        "/img/glider.png"
                      :description "Spielend Programmieren lernen"
                      :image       "https://clojure.org/images/clojure-logo-120b.png"})
        (update :base/head (fn [head]
                             (concat [[:link {:rel "stylesheet" :href (css-path)}]
                                      [:script {:src "https://unpkg.com/htmx.org@1.8.4"}]
                                      [:script {:src "https://unpkg.com/hyperscript.org@0.9.3"}]]
                                     head))))
    body))

(defn page [opts & body]
  (base
    opts
    [:.p-3.mx-auto.max-w-screen-sm.w-full
     (when (bound? #'csrf/*anti-forgery-token*)
       {:hx-headers (cheshire/generate-string
                      {:x-csrf-token csrf/*anti-forgery-token*})})
     [:header.bg-zinc-800.py-2.text-white.font-mono.flex.items-center.justify-center
      [:div
       "code "
       [:span.text-red-700 "//"]
       " editor"]]
     [:.bg-orange-50.flex.flex-col.flex-grow
      [:.grow]
      [:.p-3.mx-auto.max-w-screen-sm.w-full
       body]
      [:div {:class "grow-[2]"}]]]))
