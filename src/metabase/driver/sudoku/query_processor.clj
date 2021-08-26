(ns metabase.driver.sudoku.query-processor
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))

(defn- solve-board [hints & {:keys [max-solutions], :or {max-solutions 1}}]
  (let [vars       (vec (repeatedly 81 lvar))
        rows       (mapv vec (partition 9 vars))
        cols       (apply map vector rows)
        squares    (for [corner-x (range 0 9 3)
                         corner-y (range 0 9 3)]
                     (for [x (range corner-x (+ corner-x 3))
                           y (range corner-y (+ corner-y 3))]
                       (get-in rows [x y])))]
    (run max-solutions [q]
      (== q vars)
      (everyg #(fd/in % (fd/domain 1 2 3 4 5 6 7 8 9)) vars)
      (everyg #(if (zero? (hints %)) succeed
                   (== (vars %) (hints %)))
              (range 0 81))
      (everyg fd/distinct rows)
      (everyg fd/distinct cols)
      (everyg fd/distinct squares))))

(defn- random-solved-board []
  (or (first (solve-board
              ;; stick 10 rand digits in a grid & try to solve
              (loop [[position & more] (take 10 (shuffle (range 0 81))), board (vec (repeat 81 0))]
                (if-not position
                  board
                  (recur more (assoc board position (inc (rand-int 9))))))))
      ;; if unsolvable try again
      (recur)))

(defn- random-board [difficulty]
  {:pre [(#{:easy :medium :hard} difficulty)]}
  (let [num-holes        (- 81 ({:easy 48, :medium 36, :hard 24} (keyword difficulty)))
        solved-board     (vec (random-solved-board))
        holes-seq        (shuffle (range 0 81))
        unique-solution? #(= 1 (count (solve-board % :max-solutions 2)))]
    (loop [[hole & more] holes-seq, remaining-holes num-holes, board solved-board]
      (cond
        (zero? remaining-holes)
        board

        ;; if we run out of possible holes to dig start over with shuffled sequence of hole positions
        (not hole)
        (recur (shuffle holes-seq) num-holes solved-board)

        :else
        (let [new-board (assoc board hole 0)]
          ;; try digging a hole
          (if (unique-solution? new-board)
            ;; if board is still solvable, recurse with new board state
            (recur more (dec remaining-holes) new-board)
            ;; otherwise throw out the bad hole position and recurse
            (recur more remaining-holes board)))))))

(defn column-metadata []
  {:cols (mapv
          (fn [i]
            {:name           (format "col_%d" i)
             :base_type      :type/Integer
             :effective_type :type/Integer})
          (range 1 10))})

(defn random-board-rows [difficulty]
  (for [row (partition 9 (random-board difficulty))]
    ;; replace the zeroes in the board with nils
    (for [cell row]
      (when-not (zero? cell)
        cell))))
