(ns ile.middleware
  (:require
    [clojure.tools.logging :as log]
    [ile.env :refer [defaults]]
    [muuntaja.middleware :refer [wrap-format wrap-params]]
    [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
    [ile.mount.config :refer [env]]
    [ile.middleware.formats :as formats]
    [ring-ttl-session.core :refer [ttl-memory-store]]
    [ring.middleware.defaults :as ring-defaults]
    [ile.layout :refer [error-page]]
    [buddy.auth :refer [authenticated? throw-unauthorized]]
    [ring.middleware.session :refer [wrap-session]]
    [buddy.auth.backends :as backends]
    [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
    [rum.core :as rum]))

(defn wrap-internal-error [handler]
  (let [error-result (fn [^Throwable t]
                       (log/error t (.getMessage t))
                       (error-page {:status  500
                                    :title   "Something very bad has happened!"
                                    :message "We've dispatched a team of highly trained gnomes to take care of the problem."}))]
    (fn wrap-internal-error-fn
      ([req respond _]
       (handler req respond #(respond (error-result %))))
      ([req]
       (try
         (handler req)
         (catch Throwable t
           (error-result t)))))))



(defn wrap-csrf
  "Checks each post request for a valid csfr (anti-forgery) token.
  Add a `timebooking2.views.core/hidden-anti-forgery-field` to each form to prevent unwanted errors."
  [handler]
  (wrap-anti-forgery
    handler
    {:error-response
     (error-page
       {:status 403
        :title  "Invalid anti-forgery token"})}))

(defn wrap-formats [handler]
  (let [wrapped (-> handler wrap-params (wrap-format formats/instance))]
    (fn [request]
      ;; disable wrap-formats for websockets
      ;; since they're not compatible with this middleware
      ((if (:websocket? request) handler wrapped) request))))

(defn wrap-render-rum
  "Creates a proper ring response map if it is a hiccup vector.
  Else do nothing and assume correct ring response is present.

  Based on: https://github.com/jacobobryant/biff - Copyright (c) 2023 Jacob O'Bryant
  License:  https://github.com/jacobobryant/biff/blob/master/LICENSE"
  [handler]
  (fn [req]
    (clojure.pprint/pprint req)
    (let [response (handler req)]
      (if (vector? response)
        {:status  200
         :headers {"content-type" "text/html"}
         :body    (str "<!DOCTYPE html>\n" (rum/render-static-markup response))}
        response))))


(defn wrap-redirect-signed-in [handler]
  (fn [{:keys [session] :as req}]
    (if (some? (:uid session))
      {:status 303
       :headers {"location" "/app"}}
      (handler req))))

(defn wrap-unauthorized-login-redirect
  "Redirect unauthenticated users to the login-page. Or do nothing."
  [handler]
  (fn [request]
    (let [session (:session request)
          original-uri (:uri request)]
      (if (or (some? session)
              (= original-uri "/login"))
        (handler request)
        (assoc-in (ring.util.response/redirect "/login-page")
                  [:session :original-uri] original-uri)))))


#_(defn unauthorized-handler
  [request metadata]
  (cond
    ;; If request is authenticated, raise 403 instead
    ;; of 401 (because user is authenticated but permission
    ;; denied is raised).
    (authenticated? request)
    (-> (render (slurp (io/resource "error.html")) request)
        (assoc :status 403))
    ;; In other cases, redirect the user to login page.
    :else
    (let [current-url (:uri request)]
      (redirect (format "/login?next=%s" current-url)))))

(defn wrap-signed-in [handler]
  (fn [{:keys [session] :as req}]
    (if (some? (:uid session))
      (handler req)
      {:status 303
       :headers {"location" "/"}})))

(defn wrap-ile-defaults [h]
  (ring-defaults/wrap-defaults h
                               (-> ring-defaults/site-defaults
                                   (assoc-in [:security :anti-forgery] false)
                                   (assoc-in [:session :store] (ttl-memory-store (* 60 30)))
                                   ; enabling cookies for authentication cross-site requests
                                   (assoc-in [:session :cookie-attrs :same-site] :lax))))

;; Create an instance
(def backend (backends/session))


(defn wrap-base [handler]
  (clojure.pprint/pprint (:request handler))
  (-> ((:middleware defaults) handler)
      (wrap-authentication backend)
      wrap-session

      wrap-ile-defaults
      wrap-internal-error))