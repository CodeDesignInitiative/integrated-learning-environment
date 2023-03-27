(defproject integrated-development-environment "0.1.0-SNAPSHOT"

  :description "An online IDE for learning programming"
  :url "https://github.com/CodeDesignInitiative/integrated-learning-environment"

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.logging "1.2.4"]
                 [org.clojure/core.async "1.6.673"]
                 [org.clojure/tools.cli "1.0.214"]
                 [ch.qos.logback/logback-classic "1.4.6"]
                 [com.xtdb/xtdb-core "1.23.1"]
                 [com.xtdb/xtdb-jdbc "1.23.1"]
                 [luminus-http-kit "0.2.0"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [metosin/muuntaja "0.6.8"]
                 [metosin/reitit "0.5.18"]
                 [metosin/ring-http-response "0.9.3"]
                 [mount "0.1.17"]
                 [nrepl "1.0.0"]
                 [org.clojure/tools.cli "1.0.214"]
                 [org.clojure/tools.logging "1.2.4"]
                 [org.postgresql/postgresql "42.5.4"]
                 [ring/ring-core "1.9.6"]
                 [ring/ring-defaults "0.3.4"]
                 [rum "0.12.10" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [org.apache.commons/commons-email "1.5"]
                 [org.slf4j/slf4j-simple "2.0.5"]
                 [buddy/buddy-auth "3.0.1"]
                 [cprop "0.1.19"]
                 [simple-email/simple-email "1.0.8"]

                 [crypto-password "0.3.0"]]

  :min-lein-version "2.0.0"

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot ile.core

  :plugins []


  :aliases {"test"     ["run" "-m" "kaocha.runner" "unit"]
            "e2e"      ["run" "-m" "kaocha.runner" "e2e"]
            "test-all" ["run" "-m" "kaocha.runner" "all"]}

  :profiles
  {:uberjar       {:omit-source    true
                   :aot            :all
                   :uberjar-name   "ile.jar"
                   :source-paths   ["env/prod/clj"]
                   :resource-paths ["env/prod/resources"]}


   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev   {:jvm-opts       ["-Dconf=dev-config.edn"]
                   :dependencies   [[org.clojure/tools.namespace "1.3.0"]
                                    [pjstadig/humane-test-output "0.11.0"]
                                    [prone "2021-04-23"]
                                    [ring/ring-devel "1.9.6"]
                                    [ring/ring-mock "0.4.0"]
                                    [ring-refresh/ring-refresh "0.1.3"]
                                    ; override for etaoin
                                    [cheshire "5.11.0"]
                                    ; testcontainer
                                    [clj-test-containers "0.7.4"]
                                    [org.testcontainers/postgresql "1.17.6"]
                                    ; kaocha
                                    [lambdaisland/kaocha "1.0.861"]
                                    [lambdaisland/kaocha-cloverage "1.0-45"]
                                    [lambdaisland/kaocha-junit-xml "0.0.76"]
                                    ; etaoin
                                    [etaoin "1.0.39"]]
                   :plugins        [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                    [jonase/eastwood "0.3.5"]]


                   :source-paths   ["env/dev/clj"]
                   :resource-paths ["env/dev/resources"]
                   :repl-options   {:init-ns user
                                    :timeout 120000}
                   :injections     [(require 'pjstadig.humane-test-output)
                                    (pjstadig.humane-test-output/activate!)]}
   :project/test  {:jvm-opts       ["-Dconf=test-config.edn"]
                   :resource-paths ["env/test/resources"]}
   :profiles/dev  {}
   :profiles/test {}})
