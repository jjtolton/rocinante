(ns {{base}}.procs
  (:require
    [{{base}}.events :as events]
    [{{base}}.handlers :as handlers]
    [{{base}}.data :as data]
    [cljs.core.async :as a])
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]]))




(defonce event-loop
         (let [event-channel (data/events-channel)
               active? (atom true)]
           (letfn [(stop! [] (reset! active? false))
                   (notification! [] (js/console.log "Initializing event loop"))
                   (start! []
                     (go
                       (while @active?
                         (when-let [event (a/<! event-channel)]
                           (println "recieved event:" event)
                           (cljs.pprint/pprint
                             (into
                               []
                               (comp
                                 (map data/events!)
                                 (map events/event)
                                 (map handlers/handle!)
                                 cat)
                               [event]))))))]
             (notification!)
             (start!)
             stop!)))

(defn event-loop! [] event-loop)









