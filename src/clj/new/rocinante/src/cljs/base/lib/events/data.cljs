(ns {{base}}.lib.events.data
  (:require [{{base}}.lib.utils :as utils]))



(defmulti event! utils/event-subtype)

(defmethod event! :default [e]
  (js/console.warn (str "No data event subtype defined for" (utils/event-subtype e))))

(defmethod event! :transact [e]
  {:data/transact [(utils/event-data e)]})

