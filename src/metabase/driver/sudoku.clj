(ns metabase.driver.sudoku
  (:require [metabase.driver :as driver]
            [metabase.query-processor.store :as qp.store]
            [metabase.driver.sudoku.query-processor :as sudoku.qp]))

(driver/register! :sudoku)

(defmethod driver/supports? [:sudoku :basic-aggregations] [_ _] false)

(defmethod driver/can-connect? :sudoku [_ _]
  true)

(defmethod driver/describe-database :sudoku [_ _]
  {:tables
   (set
    (for [table-name ["easy"
                      "medium"
                      "hard"]]
      {:name   table-name
       :schema nil}))})

(defmethod driver/describe-table :sudoku [_ _ {table-name :name}]
  {:name   table-name
   :schema nil
   :fields (set (for [i (range 1 10)]
                  {:name          (format "col_%d" i)
                   :database-type "org.metabase.enterprise_sudoku.NewColumnFactoryAbstractColumnProxyImpl"
                   :base-type     :type/Integer}))})

(defmethod driver/mbql->native :sudoku [_ {{source-table-id :source-table} :query, :as mbql-query}]
  (println "mbql-query:" mbql-query) ; NOCOMMIT
  (:name (qp.store/table source-table-id)))

(defmethod driver/execute-query :sudoku [_ {{difficulty :query} :native, :as native-query}]
  (println "native-query:" native-query) ; NOCOMMIT
  (sudoku.qp/rando-board-query-results difficulty))
