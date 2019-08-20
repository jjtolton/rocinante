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
                           (try
                             (println (into (sorted-map) (seq event)))
                             (into
                               []
                               (comp
                                 (map data/events!)
                                 (map events/event)
                                 (map handlers/handle!)
                                 cat)
                               [event])
                             (catch :default e
                               (js/console.error e))
                             )))))]
             (notification!)
             (start!)
             stop!)))

(defn event-loop! [] event-loop)
