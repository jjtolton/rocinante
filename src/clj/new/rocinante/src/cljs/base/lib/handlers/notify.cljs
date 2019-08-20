(ns {{base}}.lib.handlers.notify
  (:require [antd]
            [{{base}}.lib.utils :as utils]))

(defmulti -handle! :subtype)

(defn handle! [e] (-handle! e))

(defmethod -handle! :default [{type :event}]
           (println "[-] No notification message defined for " type))

(defmethod -handle! :success [e] (js/antd.message.success (utils/event-data e)))
(defmethod -handle! :info [e] (js/antd.message.info (utils/event-data e)))
(defmethod -handle! :loading [e] (js/antd.message.loading (utils/event-data e)))
(defmethod -handle! :warn [e] (js/antd.message.warn (utils/event-data e)))
(defmethod -handle! :error [e] (js/antd.message.error (utils/event-data e)))

