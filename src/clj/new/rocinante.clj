(ns clj.new.rocinante
  (:require [clj.new.templates :refer [renderer project-name name-to-path ->files]]))

(def render (renderer "rocinante"))


(defn cljs-template! [name]
  (let [data {:name      (project-name name)
              :base      (clojure.string/replace
                           (project-name name)
                           #"(.*?)[.](.*$)"
                           "$1")
              :sanitized (name-to-path name)}]
    (println "Generating ClojureScript template for" (:name data) "at" (:sanitized data))

    (letfn [(render-it
              [path-name]
              [path-name
               (render
                 (str "cljs/"
                      (clojure.string/replace path-name #"\{\{base\}\}" "base"))
                 data)])]
      (->files data

               ;; resources
               "resources/public/css"
               "resources/public/html"
               "resources/public/js"
               ["resources/public/index.html"
                (render "cljs/base/resources/index.html" data)]

               ;; scripts
               "scripts"

               ;; src/cljs/{{base}}
               (render-it "src/cljs/{{base}}/lib/data/db.cljs")
               (render-it "src/cljs/{{base}}/lib/data/ds.cljs")
               (render-it "src/cljs/{{base}}/lib/events/data.cljs")
               (render-it "src/cljs/{{base}}/lib/events/init.cljs")
               (render-it "src/cljs/{{base}}/lib/events/notify.cljs")
               (render-it "src/cljs/{{base}}/lib/handlers/notify.cljs")
               (render-it "src/cljs/{{base}}/lib/events.cljs")
               (render-it "src/cljs/{{base}}/lib/handlers.cljs")
               (render-it "src/cljs/{{base}}/lib/procs.cljs")
               (render-it "src/cljs/{{base}}/lib/reactions.cljs")
               (render-it "src/cljs/{{base}}/lib/utils.cljs")
               "src/cljs/{{base}}/lib/procs"
               "src/cljs/{{base}}/lib/reactions"
               (render-it "src/cljs/{{base}}/lib/data.cljs")
               (render-it "src/cljs/{{base}}/lib/events.cljs")
               (render-it "src/cljs/{{base}}/lib/handlers.cljs")
               (render-it "src/cljs/{{base}}/lib/procs.cljs")
               (render-it "src/cljs/{{base}}/lib/reactions.cljs")
               (render-it "src/cljs/{{base}}/lib/utils.cljs")
               (render-it "src/cljs/{{base}}/views/main.cljs")
               (render-it "src/cljs/{{base}}/config.cljs")
               (render-it "src/cljs/{{base}}/core.cljs")
               (render-it "src/cljs/{{base}}/data.cljs")
               (render-it "src/cljs/{{base}}/events.cljs")
               (render-it "src/cljs/{{base}}/handlers.cljs")
               (render-it "src/cljs/{{base}}/procs.cljs")
               (render-it "src/cljs/{{base}}/reactions.cljs")
               (render-it "src/cljs/{{base}}/worker.cljs")

               ;; src/js
               (render-it "src/js/index.js")





               ;; project config files
               (render-it "Makefile")
               (render-it "deps.edn")
               (render-it "bridge.sh")
               (render-it "build.sh")
               (render-it "deps.edn")
               (render-it "dev.cljs.edn")
               (render-it "figwheel.clj")
               (render-it "figwheel-help.txt")
               (render-it "figwheel-main.edn")
               (render-it "Makefile")
               (render-it "README.md")
               (render-it "setup.sh")
               (render-it "webpack.config.js")
               (render-it "worker.cljs.edn"))))
  )



(defn rocinante
  [name & args]
  (cond
    (some #{"--cljs" "cljs" "+cljs"} args) (cljs-template! name)
    :else (clojure.pprint/pprint {:project/name      (project-name name)
                                  :project/sanitized (name-to-path name)
                                  :project/base      (clojure.string/replace
                                                       (project-name name)
                                                       #"(.*?)[.](.*$)"
                                                       "$1")
                                  :cli/args          args
                                  :clj/name          name})
    )

  )


(comment
  (letfn [(render-it
            [path-name]
            [(clojure.string/replace path-name #"\{\{base\}\}" "base")
             #_(render (str "cljs/" path-name) data)])]

    (render-it "src/cljs/{{base}}/lib/procs.cljs"

               )))



