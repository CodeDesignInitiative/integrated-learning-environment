(ns cd.ile.feat.home
  (:require [com.biffweb :as biff]
            [cd.ile.middleware :as mid]
            [cd.ile.ui :as ui]
            [cd.ile.util :as util]))

(defn signin-form [{:keys [recaptcha/site-key] :as sys}]
  (biff/form
    {:id "signin-form"
     :action "/auth/send"
     :class "sm:max-w-xs w-full"}
    [:input#email
     {:name "email"
      :type "email"
      :autocomplete "email"
      :placeholder "Enter your email address"
      :class '[border
               border-gray-300
               rounded
               w-full
               focus:border-teal-600
               focus:ring-teal-600]}]
    [:.h-3]
    [:button
     (merge
       (when (util/email-signin-enabled? sys)
         {:data-sitekey site-key
          :data-callback "onSubscribe"
          :data-action "subscribe"})
       {:type "submit"
        :class '[bg-teal-600
                 hover:bg-teal-800
                 text-white
                 py-2
                 px-4
                 rounded
                 w-full
                 g-recaptcha]})
     "Join the waitlist"]))

(defn home [sys]
  (ui/base
    {}
    [:.bg-orange-50.flex.flex-col.flex-grow.items-center.p-3
     [:.h-12.grow]
     ;[:img.w-40 {:src "/img/eel.svg"}]
     [:.h-6]
     [:.text-2xl.sm:text-3xl.font-semibold.sm:text-center.w-full
      "Programmieren für alle"]
     [:.h-2]
     [:.sm:text-lg.sm:text-center.w-full
      "Webseiten, Spiele und Apps."]
     [:.h-6]
     (signin-form sys)
     [:.h-12 {:class "grow-[2]"}]
     [:.h-6]]))

(def features
  {:routes ["" {:middleware [mid/wrap-redirect-signed-in]}
            ["/" {:get home}]]})
