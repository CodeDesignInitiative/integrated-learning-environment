(ns ile.routes.public
  (:require [ile.ui.auth.core :as auth-page]))


(def routes
  auth-page/routes)
