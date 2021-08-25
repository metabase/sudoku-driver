(ns metabase.driver.sudoku
  (:require [clojure.pprint :as pprint]
            [metabase.driver :as driver]
            [metabase.driver.sudoku.query-processor :as sudoku.qp]
            [metabase.query-processor.store :as qp.store]))

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
  [_driver _database {table-name :name}]
  {:name   table-name
   :schema nil
   :fields (set (for [i (range 1 10)]
                  {:name              (format "col_%d" i)
                   :database-type     "org.metabase.enterprise_sudoku.NewColumnFactoryAbstractColumnProxyImpl"
                   :base-type         :type/Integer
                   :database-position (dec i)}))})

(defmethod driver/mbql->native :sudoku
  [_driver {{source-table-id :source-table} :query, :as mbql-query}]
  (println "MBQL query:")
  (pprint/pprint mbql-query)
  (:name (qp.store/table source-table-id)))

(defmethod driver/execute-reducible-query :sudoku
  [_driver {difficulty :native, :as query} _context respond]
  (println "Native query:" (pr-str (select-keys query [:native])))
  (let [metadata (sudoku.qp/column-metadata)
        rows     (sudoku.qp/random-board-rows (keyword difficulty))]
    (respond metadata rows)))
