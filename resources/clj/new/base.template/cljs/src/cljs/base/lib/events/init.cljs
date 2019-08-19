(ns {{base}}.lib.events.init
  (:require
    [{{base}}.lib.utils :as utils]
    [{{base}}.data :as data]))


(defmulti event! utils/event-subtype)

(defmethod event! :default [e]
  (js/console.warn "No init subtype defined for" (utils/event-subtype e)))

(defmethod event! :init
  [e]
  {:notify/success [(utils/event-data e)]})


