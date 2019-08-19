(ns {{base}}.lib.handlers
  (:require
    [clojure.core.async]
    [{{base}}.lib.utils :as utils]
    [{{base}}.lib.handlers.notify :as notify]
    )
  (:require-macros
    [cljs.core.async.macros :as am :refer [go]]))


(defmulti handle! utils/event)

(defmethod handle! :default [e]
  (js/console.warn "No handler type defined for" (utils/event e)))

(defmethod handle! :notify [e] (notify/handle! e))

