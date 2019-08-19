(ns {{base}}.handlers
  (:require [{{base}}.lib.handlers]
            [{{base}}.lib.utils :as utils])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))



(defprotocol IHandle
  (handle! [e]))

(letfn
  [(handle-mapping [event-mappings]
     (let [event-maps
           (for [[event-type event-data] (seq event-mappings)
                 {event :event :as event-map} event-data]
             {:event   event-type
              :type    (utils/event->type event)
              :subtype (utils/event->subtype event)
              :data    (utils/event-data event-map)})]
       event-maps))
   (handle-event-mappings [handler-mappings]
     (doseq [handler-mapping handler-mappings]
       (go ({{base}}.lib.handlers/handle! handler-mapping)))
     handler-mappings
     )]

  (def handle-mapping handle-mapping)
  (let [handle-mapping
        (comp handle-event-mappings handle-mapping)]
    (extend-protocol IHandle
      clojure.core/PersistentArrayMap
      (handle! [events]
        "{:event-type [{:event :some/type :data 12345} event1 event2 ...], :event-type1 event-data1}"
        (handle-mapping events))
      clojure.core/PersistentArrayMap
      (handle! [events]
        "{:event-type [{:event :some/type :data 12345} event1 event2 ...], :event-type1 event-data1}"
        (handle-mapping events))))
  )

