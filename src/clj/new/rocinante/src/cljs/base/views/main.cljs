(ns {{base}}.views.main
  (:require [{{base}}.events :as events]
            [{{base}}.reactions :as rx]
            [{{base}}.data :as data]
            [{{base}}.lib.utils :as utils]
            [{{base}}.lib.data.ds :as ds]))


(defn todo-table [items]
  [:table
   {:style {:flex-grow 3}}
   [:thead
    [:tr
     [:th "Done"]
     [:th "Rank"]
     [:th "Item"]
     [:th]
     ]]
   [:tbody
    (doall
      (for [{status     :item/status
             item-mode  :item/mode
             id         :item/id
             status-key :item.key/status
             rank       :item/order
             desc-key   :item.key/description}
            items]

        ^{:key (hash [id status-key desc-key])}
        [:tr
         [:td ^{:key status-key} [:input
                                  {:on-change
                                         (if (= :item.status/done status)
                                           #(events/event! :item/not-done id)
                                           #(events/event! :item/done id))


                                   :type :checkbox
                                   :checked
                                         (= :item.status/done status)}]]
         [:td rank]
         [:td ^{:key desc-key} [:div
                                (if (= item-mode :item.mode/edit)
                                  [:input
                                   {:value @(rx/item-description id)
                                    :on-change
                                           #(events/event!
                                              {:event :item/edit
                                               :data  {:item/id          id
                                                       :item/description (utils/evt-value %)}})

                                    :on-key-down
                                           #(when (utils/evt-submit? %)
                                              (events/event! :item/read-mode id))}]

                                  [:div
                                   {:on-click #(events/event! :item/edit-mode id)}
                                   @(rx/item-description id)])]]
         [:td
          [:button {:on-click #(events/event! :item/promote id)} "up"]
          [:button {:on-click #(events/event! :item/demote id)} "down"]]

         ]))]])

(defn main-panel []
  [:div

   {:style {:display :flex}}
   [todo-table (sort-by :item/order @(rx/todo-items))]

   [:div
    [:button
     {:on-click
             #(events/event! {:event :notify/success
                              :data  "Yo!!"})
      :style {:flex-grow 0}}
     "Yooo!!"]

    [:ol
     (doall (for [e @(data/events!)] ^{:key ((comp str random-uuid))}
                                     [:li (str e)]))]]])
