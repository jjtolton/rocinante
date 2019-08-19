(ns {{base}}.lib.events
  (:require [{{base}}.lib.utils :as utils]
            [{{base}}.lib.events.init :as init]
            [{{base}}.lib.events.notify :as notify]
            [{{base}}.lib.events.data :as data]))

(defmulti event utils/event-type)

(defmethod event :init [e] (init/event! e))

(defmethod event :notify [e] (notify/event! e))

(defmethod event :data [e] (data/event! e))

