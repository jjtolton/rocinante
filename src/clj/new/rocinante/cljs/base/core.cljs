(ns ^:figwheel-hooks {{base}}.core
  (:require
    [reagent.core :as reagent]
    [{{base}}.config :as config]
    [{{base}}.views.main :as views.main]
    [{{base}}.data :as data]
    [{{base}}.procs :as procs]
    [{{base}}.events :as events]
    [cljs.core.async :as a]

    material-ui
    material-icons
    antd)
  (:require-macros
    [dommy.core :refer [sel1]]
    [cljs.core.async.macros :refer [go go-loop]]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (reagent/render [views.main/main-panel] (sel1 :#app)))

(defn ^:after-load js-reload []
  (mount-root))

(defn ^:export init []
  (dev-setup)
  (procs/event-loop!)
  (mount-root))


(comment
  ({{base}}.handlers/handle-mapping (events/event {:event :notify/success :data "hey"}))

  (events/event! {:event :notify/success :data  "heyyy"})

  )
