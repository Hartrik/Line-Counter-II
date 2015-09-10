; comment 1
(ns Math.core)

(defn pi
  [precision]
  (let [numerators (cycle [1 -1])
        denominators (iterate #(+ 2 %) 1)
        fractions (map / numerators denominators)]  ; comment 2
    (* 4 (reduce + (take precision fractions)))))

(defn print-pi  ; comment 3
  (doseq [x (map #(double (pi %)) (range 20))] (println x)))

(def str-1 "; this is not comment")  ; comment 4
(def str-2 " multiline
            ; this is not comment")  ; comment 5
(def str-3 (str \space \" \space \;))  ; comment 6

; comment 7