(ns cd.ile.feat.app
  (:require [com.biffweb :as biff :refer [q]]
            [cd.ile.middleware :as mid]
            [cd.ile.ui :as ui]
            [rum.core :as rum]
            [xtdb.api :as xt]
            [ring.adapter.jetty9 :as jetty]
            [cheshire.core :as cheshire]
            [cd.ile.views.home-screen :as home-screen]))

(defn app [{:keys [session biff/db] :as req}]
  (let [{:user/keys [email]} (xt/entity db (:uid session))]
    (ui/page
      {}
      nil
      ;[:div "Signed in as " email ". "
      ; (biff/form
      ;   {:action "/auth/signout"
      ;    :class  "inline"}
      ;   [:button.text-blue-500.hover:text-blue-800 {:type "submit"}
      ;    "Sign out"])
      ; "."]
      (home-screen/home-screen)
      )))


(def features
  {:routes ["/app" {:middleware [mid/wrap-signed-in]}
            ["" {:get app}]]})
