(ns {{base}}.worker
  "Reserved for future use.

  In order to compile worker, use this from the command line.
  clj -R:nREPL -m figwheel.main -bo worker

  this entry must be keyed to :nREPL in the :alias section
  of deps.edn

  :nREPL
  {:extra-deps
   {nrepl/nrepl                     {:mvn/version \"0.6.0\"}
    cider/piggieback                {:mvn/version \"0.4.0\"}
    com.bhauman/figwheel-main       {:mvn/version \"0.2.0\"}
    com.bhauman/rebel-readline-cljs {:mvn/version \"0.1.4\"}}

  and worker.cljs.edn must be included as well


  "
  (:require [cljs-workers.core :as main]))



(defn -main []
  (println "hello")
  (set! (.-onmessage js/self)
        (fn [e]
          (println "got it!")
          (println e)
          (js/postMessage "sent!!"))))

(when (main/worker?)
  (-main))

#_(when (not (main/worker?))
  (set!
    (.-worker js/window)
    (js/Worker. "js/compiled/worker.js"))
  )



