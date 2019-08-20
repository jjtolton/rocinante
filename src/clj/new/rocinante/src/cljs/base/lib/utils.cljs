(ns {{base}}.lib.utils)


(defn event [{event :event}] event)


(defn event->type [event] (-> event namespace keyword))
(defn event->subtype [event] (-> event name keyword))
(defn event-type [{event :event}] (event->type event))

(defn event-subtype [{event :event}] (event->subtype event))

(defn event-data [{data :data}] data)

(defn event-key [e]
      (-> e .-key))

(defn evt-submit? [e]
      (-> e event-key (= "Enter")))

(defn evt-value [e]
      (-> e .-target .-value))
