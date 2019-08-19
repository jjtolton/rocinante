(ns {{base}}.events
  (:require
    [{{base}}.lib.events]
    [{{base}}.lib.utils :as utils]
    [cljs.core.async :as a]
    [{{base}}.data :as data]
    [cljs-time.core :as t]
    [cljs-time.coerce :as c]
    [cljs-time.format :as f]))


(defn event [e]
  (let [event-type (utils/event-type e)]
    (if event-type
      ({{base}}.lib.events/event e)
      (js/console.warn "[-] No event registered for " event-type))))


(defprotocol IEvent
  (event! [e] [k v]))


(extend-protocol IEvent
  clojure.core/PersistentVector
  (event! [e] (a/onto-chan
                (data/events-channel)
                (into [] (map #(assoc % :timestamp (t/now))) e)
                false))

  clojure.core/PersistentHashMap
  (event! [e] (a/offer! (data/events-channel)
                        (assoc e :timestamp (t/now))))

  clojure.core/PersistentArrayMap
  (event! [e] (a/offer! (data/events-channel) (assoc e :timestamp (t/now))))


  clojure.core/Keyword
  (event!
    ([e] (a/offer! (data/event-channel)
                   {:timestamp (t/now)
                    :event     e}))

    ([k v] (a/offer! (data/event-channel)
                     {:timestamp (t/now)
                      :event     k
                      :data      v}))))




