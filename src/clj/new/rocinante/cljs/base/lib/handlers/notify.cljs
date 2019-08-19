(ns {{base}}.lib.handlers.notify
  (:require [ant-message]
            [{{base}}.lib.utils :as utils]))

(defmulti handle! :subtype)

(defmethod handle! :default [{type :event}]
  (println "[-] No notification message defined for " type))

(defmethod handle! :success [e] (.success ant-message (utils/event-data e)))
(defmethod handle! :info [e] (.info ant-message (utils/event-data e)))
(defmethod handle! :loading [e] (.loading ant-message (utils/event-data e)))
(defmethod handle! :warn [e] (.warn ant-message (utils/event-data e)))
(defmethod handle! :error [e] (.error ant-message (utils/event-data e)))

