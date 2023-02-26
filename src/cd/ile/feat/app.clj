(ns cd.ile.feat.app
  (:require [com.biffweb :as biff]
            [cd.ile.middleware :as mid]
            [cd.ile.ui :as ui]
            [xtdb.api :as xt]
            [cd.ile.views.chat-screen :as chat-screen]
            [cd.ile.views.wiki-screen :as wiki-screen]
            [cd.ile.views.jobs-screen :as jobs-screen]
            [cd.ile.views.editor-screen :as editor-screen]
            [cd.ile.views.home-screen :as home-screen]))

(defn app [{:keys [session biff/db] :as req}]
  (let [{:user/keys [email]} (xt/entity db (:uid session))]
    (ui/page {} nil (home-screen/home-screen))))

(defn chat [{:keys [session biff/db] :as req}]
  (let [{:user/keys [email]} (xt/entity db (:uid session))]
    (ui/page {} nil (chat-screen/chat-screen))))
(defn wiki [{:keys [session biff/db] :as req}]
  (let [{:user/keys [email]} (xt/entity db (:uid session))]
    (ui/page {} nil (wiki-screen/wiki-screen))))

(defn jobs [{:keys [session biff/db] :as req}]
  (let [{:user/keys [email]} (xt/entity db (:uid session))]
    (ui/page {} nil (jobs-screen/jobs-screen))))

(defn editor [{:keys [session biff/db] :as req}]
  (let [{:user/keys [email]} (xt/entity db (:uid session))]
    (ui/page {} nil (editor-screen/editor-screen))))


(def features
  {:routes ["/app" {:middleware [mid/wrap-signed-in]}
            ["" {:get app}]
            ["/chat" {:get chat}]
            ["/wiki" {:get wiki}]
            ["/auftraege" {:get jobs}]
            ["/editor" {:get editor}]
            ]})
