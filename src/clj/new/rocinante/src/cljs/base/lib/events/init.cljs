(ns {{base}}.lib.events.init
  (:require
    [{{base}}.lib.utils :as utils]
    [{{base}}.data :as data]))


(defmulti event! utils/event-subtype)

(defmethod event! :default [e]
  (js/console.warn "No init subtype defined for" (utils/event-subtype e)))

(defmethod event! :init
  [_]
           {:notify [{:event :notify/success
                      :data  "Application initialized!"}]
            :data   [{:event :data/transact
                      :data  [{:db/ident      :app/data
                               :app.data/name "{{base}}.example1"}

                              {:item/id          ((comp str random-uuid))
                               :item/description "Adapt this template!"
                               :item/status      :item.status/incomplete
                               :item/mode        :item.mode/read
                               :item.key/status    ((comp str random-uuid))
                               :item.key/description ((comp str random-uuid))
                               :item/order 0}

                              {:item/id          ((comp str random-uuid))
                               :item/description "Looking good!"
                               :item/status      :item.status/done
                               :item/mode        :item.mode/read
                               :item.key/status    ((comp str random-uuid))
                               :item.key/description ((comp str random-uuid))
                               :item/order 1}

                              {:item/id          ((comp str random-uuid))
                               :item/description "Click me to edit!"
                               :item/status      :item.status/done
                               :item/mode        :item.mode/read
                               :item.key/status    ((comp str random-uuid))
                               :item.key/description ((comp str random-uuid))
                               :item/order 2}

                              {:item/id          ((comp str random-uuid))
                               :item/description "\"Enter\" to go to confirm changes!"                      :item/status      :item.status/done
                               :item/mode        :item.mode/edit
                               :item.key/status    ((comp str random-uuid))
                               :item.key/description ((comp str random-uuid))
                               :item/order 3}

                              ]}]})


