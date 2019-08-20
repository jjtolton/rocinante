(ns {{base}}.lib.handlers.data
  (:require [{{base}}.lib.data.ds :as ds]
            [{{base}}.lib.utils :as utils]
            ))

(defmulti -handle! :subtype)

(defn handle! [e] (-handle! e))

(defmethod -handle! :default [{type :event}]
  (println "[-] No data effect defined for " type))

(defmethod -handle! :transact [{data :data}]
  (ds/transact! data))
