(ns ile.middleware
  (:require
    [clojure.tools.logging :as log]
    [ile.env :refer [defaults]]
    [ile.mount.config :refer [env]]
    [ile.layout :refer [error-page] :as layout]
    [muuntaja.middleware :refer [wrap-format wrap-params]]
    [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
    [ile.middleware.formats :as formats]
    [ring-ttl-session.core :refer [ttl-memory-store]]
    [ring.util.response :as response]
    [ring.middleware.defaults :as ring-defaults]
    [buddy.auth :refer [authenticated?]]
    [ring.middleware.session :refer [wrap-session]]
    [buddy.auth.backends :as backends]
    [buddy.auth.middleware :refer [wrap-authentication]]))

(defn wrap-internal-error [handler]
  (let [error-result (fn [^Throwable t]
                       (log/error t (.getMessage t))
                       (error-page {:status  500
                                    :title   "Oh no!"
                                    :message "Da ist wohl etwas schief gelaufen. Sollte der Fehler wiederkommen, kannst du eine Mail an paul.hempel@code.design schreiben mit Informationen was genau passiert ist und wir kümmern uns um den Fehler :)"}))]
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
  Add a `ile.views.core/hidden-anti-forgery-field` to each form to prevent unwanted errors."
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
  "Create HTML response if Hiccup vector present"
  [handler]
  (fn [request]
    (let [response (handler request)]
      (if (vector? response) (layout/render-page response) response))))

(defn wrap-unauthorized-login-redirect
  "Redirect unauthenticated users to the login-page. Or do nothing."
  [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      (let [original-uri (:uri request)]
        (response/redirect
          (if (or (= original-uri "/")
                  (= original-uri "/logout"))
            "/de/login"
            (str "/login?next=" original-uri)))))))

;(defn- is-admin? [email]
;  (some #{email} ["paul.hempel@code.design"
;                  ""])
;  )

(defn wrap-teacher-access [handler]
  (fn [request]
    (let [is-teacher-or-admin? (or (boolean (some #{:admin} (get-in request [:session :user :user/roles])))
                                   (boolean (some #{:teacher} (get-in request [:session :user :user/roles]))))]
      (if is-teacher-or-admin?
        (handler request)
        (response/redirect "/")))))

(defn wrap-admin-access [handler]
  (fn [request]
    (let [is-admin? (some #{:admin} (get-in request [:session :user :user/roles]))]
      (if is-admin?
        (handler request)
        (response/redirect "/")))))


(defn wrap-signed-in [handler]
  (fn [{:keys [session] :as req}]
    (if (some? (:uid session))
      (handler req)
      {:status  303
       :headers {"location" "/"}})))

(defn wrap-ile-defaults [handler]
  (ring-defaults/wrap-defaults
    handler
    (-> (if (:dev env)
          ring-defaults/site-defaults
          (-> ring-defaults/secure-site-defaults
              (assoc :proxy true)
              (assoc [:security :hsts] true)))
        #_ring-defaults/site-defaults

        (assoc-in [:security :anti-forgery] false)
        (assoc-in [:session :store] (ttl-memory-store (* 60 60 2)))
        ; enabling cookies for authentication cross-site requests
        (assoc-in [:session :cookie-attrs :same-site] :lax))))

(defn wrap-render-htmx
  [handler]
  (fn [request]
    (let [response (handler request)]
      (layout/render-htmx response))))

(defn wrap-security-header [handler]
  (fn [request]
    (if (:dev env)
      (handler request)
      (->
        (handler request)
        #_(response/header "Content-Security-Policy" "default-src https:'" #_"default-src https:; img-src 'self'; script-src 'self'; style-src 'self'; font-src 'self'; object-src 'none'")
        (response/header "header-Strict-Transport-Security" "max-age=63072000")
        (response/header "X-XSS-Protection" "1; mode=block")))))

(def backend (backends/session))

(defn wrap-base [handler]
  (->
    ((:middleware defaults) handler)
    wrap-security-header
    (wrap-authentication backend)
    ;wrap-user-to-session
    wrap-session
    wrap-ile-defaults
    wrap-internal-error))
