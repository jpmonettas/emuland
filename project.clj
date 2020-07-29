(defproject emuland "0.1.0-SNAPSHOT"

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Using leiningen just for running garden. ;;
  ;; The project is compiled with cljs        ;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [garden "1.3.10"]]

  :plugins [[lein-garden "0.3.0"]]

  :min-lein-version "2.9.0"

  :jvm-opts ["-Xmx1G"]

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/css"]


  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   emuland.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]})
