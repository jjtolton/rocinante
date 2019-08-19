(ns {{base}}.views.main
  (:require [{{base}}.events :as events]
            [{{base}}.data :as data]))


(defn main-panel []
  [:div
   [:button
    {:on-click
     #(events/event! [{:event :notify/success
                       :data  "Yo!!"}
                      {:event :notify/warn
                       :data  "eyyyy!!!"}
                      ])}
    "Yooo!!"

    ]
   [:table
    [:thead
     [:tr
      [:th "Event"]
      [:th "Data"]
      [:th "Timestamp"]]]
    [:tbody

     (doall
       (for [{:keys [event data timestamp]} @(data/events!)]
         ^{:key ((comp str random-uuid))}
         [:tr
          [:td (str event)]
          [:td (str data)]
          [:td (str timestamp)]]))]]
   ])
