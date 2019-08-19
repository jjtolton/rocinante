(ns {{base}}.lib.data
  (:require [reagent.core :as reagent]))

(defonce ^:private events (reagent/atom []))

(defn events!
  ([] events)
  ([evt] (swap! events conj evt) evt))


