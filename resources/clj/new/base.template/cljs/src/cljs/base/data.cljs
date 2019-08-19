(ns {{base}}.data
  (:require [cljs-workers.core :as main]
            [cljs.core.async :as a]
            [{{base}}.lib.data]
            ))


(def ^:private event-channel (a/chan 256))

(defn events-channel [] event-channel)

(def events! {{base}}.lib.data/events!)


