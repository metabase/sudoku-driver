(ns metabase.driver.sudoku
  (:require [metabase.driver :as driver]
            [metabase.query-processor.store :as qp.store]
            [metabase.query-processor.reducible :as qp.reducible]
            [metabase.driver.sudoku.query-processor :as sudoku.qp]))

(driver/register! :sudoku)

(defmethod driver/database-supports? [:sudoku :basic-aggregations]
  [_driver _feature _database]
  false)

;; deprecated -- this will be removed in the near future.
(defmethod driver/supports? [:sudoku :basic-aggregations]
  [_driver _feature]
  false)

(defmethod driver/can-connect? :sudoku
  [_driver _details]
  true)

(defmethod driver/describe-database :sudoku
  [_driver _database]
  {:tables
   (set
    (for [table-name ["easy"
                      "medium"
                      "hard"]]
      {:name   table-name
       :schema nil}))})

(defmethod driver/describe-table :sudoku
  [_driver _Database {table-name :name}]
  {:name   table-name
   :schema nil
   :fields (set (for [i (range 1 10)]
                  {:name          (format "col_%d" i)
                   :database-type "org.metabase.enterprise_sudoku.NewColumnFactoryAbstractColumnProxyImpl"
                   :base-type     :type/Integer}))})

(defmethod driver/mbql->native :sudoku
  [_driver {{source-table-id :source-table} :query, :as mbql-query}]
  (println "mbql-query:" mbql-query) ; NOCOMMIT
  (:name (qp.store/table source-table-id)))

(defmethod driver/execute-reducible-query :sudoku
  [_driver {{difficulty :query} :native, :as native-query} _context respond]
  (println "native-query:" native-query) ; NOCOMMIT
  (let [metadata (sudoku.qp/column-metadata)
        rows     (sudoku.qp/rando-board-query-results difficulty)]
    (respond metadata rows)))
