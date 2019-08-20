(ns {{base}}.reactions
  (:require [reagent.ratom :as ratom]
            [{{base}}.lib.data.ds :as ds]))


(defn todo-items []
  (ds/q-rx '[:find [(pull ?e [*]) ...]
             :where
             [?e :item/id]]))


(defn item-description [id]
  (ds/q-rx '[:find ?desc .
             :in $ ?id
             :where
             [?e :item/id ?id]
             [?e :item/description ?desc]]
           id))
