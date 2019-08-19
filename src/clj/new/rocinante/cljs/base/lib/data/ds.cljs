(ns {{base}}.platforms.lib.data.ds
  (:require [reagent.core :as reagent]
            [datascript.core :as d]))



(defn site-schema []
  {:db/ident          {:db/unique :db.unique/identity}

   })

(defn create-conn
  ([schema] (reagent/atom
              (d/empty-db schema)
              :meta {:listeners (atom [])}))
  ([] (create-conn nil)))


(defonce conn (create-conn (site-schema)))


(defn transact! [datoms] (d/transact! conn datoms))

(defonce schemata (atom {}))

(defn transact-schema! [schema]
  (swap! schemata merge schema)
  (swap! conn
         (fn [c]
           (d/init-db
             (d/datoms c :eavt)
             @schemata))))



(defn q [query & args] (apply d/q query @conn args))

(defn q-rx [query & args]
  (reagent.ratom/make-reaction
    #(apply
       d/q query
       @conn args)))


(defn pull [& args]
  (apply d/pull @conn args))

(defn pull-rx [& args]
  (reagent.ratom/make-reaction
    #(apply d/pull @conn args)))


(defn pull* [& args] (apply d/pull-many @conn args))

(defn pull*-rx [& args]
  (reagent.ratom/make-reaction
    #(apply d/pull-many @conn args)))


(comment



  )





