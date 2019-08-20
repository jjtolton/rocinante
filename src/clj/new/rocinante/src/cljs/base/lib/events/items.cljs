(ns {{base}}.lib.events.items
  (:require [{{base}}lib.utils :as utils]
            [{{base}}lib.data.ds :as ds]))

(defmulti event! utils/event-subtype)

(defmethod event! :done [e]
  (let [id (utils/event-data e)]
    {:data [{:event :data/transact
             :data  [{:item/id     id
                      :item/status :item.status/done}]}]}))

(defmethod event! :not-done [e]
  (let [id (utils/event-data e)]
    {:data [{:event :data/transact
             :data  [{:item/id     id
                      :item/status :item.status/incomplete}]}]}))

(defmethod event! :read-mode [e]
  (let [id (utils/event-data e)]
    {:data [{:event :data/transact
             :data  [{:item/id   id
                      :item/mode :item.mode/read}]}]}))

(defmethod event! :edit-mode [e]
  (let [id (utils/event-data e)]
    {:data [{:event :data/transact
             :data  [{:item/id   id
                      :item/mode :item.mode/edit}]}]}))

(defmethod event! :edit [e]
  (let [{id          :item/id
         description :item/description :as ed}
        (utils/event-data e)]
    {:data [{:event :data/transact
             :data  [{:item/id          id
                      :item/description description}]}]}))

(defmethod event! :promote [e]
  (let [id (utils/event-data e)]
    (let [[rank prev-id prev-rank]
          (ds/q '[:find [?order ?prev-id ?order-1]
                  :in $ ?id
                  :where
                  [?e :item/id ?id]
                  [?e :item/order ?order]
                  [(dec ?order) ?order-1]
                  [?prev-e :item/order ?order-1]
                  [?prev-e :item/id ?prev-id]]
                id)]
      (if prev-id
        {:data [{:event :data/transact
                 :data  [{:item/id id :item/order prev-rank}
                         {:item/id prev-id :item/order rank}]}]}
        {:data []}))))

(defmethod event! :demote [e]
  (let [id (utils/event-data e)]
    (let [[rank next-id next-rank]
          (ds/q '[:find [?order ?next-id ?order+1]
                  :in $ ?id
                  :where
                  [?e :item/id ?id]
                  [?e :item/order ?order]
                  [(inc ?order) ?order+1]
                  [?prev-e :item/order ?order+1]
                  [?prev-e :item/id ?next-id]]
                id)]
      (if next-id
        {:data [{:event :data/transact
                 :data  [{:item/id id :item/order next-rank}
                         {:item/id next-id :item/order rank}]}]}
        {:data []}))))
