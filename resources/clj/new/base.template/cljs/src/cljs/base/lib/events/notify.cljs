(ns {{base}}.lib.events.notify
  (:require [{{base}}.lib.utils :as utils]))

(defn event! [e]
  {:notify [{:event (utils/event e)
             :data  (utils/event-data e)}]})

