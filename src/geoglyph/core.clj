(ns geoglyph.core)

(defn bin-str-to-num
  [bin]
  (let [values (->> (map-indexed vector (reverse bin))
                    (map #(if (= (second %) \1) (Math/pow 2 (first %)) 0))
                    (reduce +)
                    (int))]
    values))

(defn encode
  [number max-val accuracy]
  (loop [encoded ""
         pos 0.0
         dir 1.0
         move-amount (* 0.5 max-val)]
    (println (prn-str [number max-val accuracy encoded pos dir move-amount]))
    (if (or (<= 128 (count encoded)) (<= (Math/abs (- pos number)) accuracy))
      encoded
      (let [delta (- number pos)
            factor (/ delta dir)
            next-digit (cond
                         (and (< (* 0.5 move-amount) (Math/abs delta))
                              (< 0 factor)) "1"
                         :else "0")
            moved (= next-digit "1")]
        (recur (str encoded next-digit)
               (if moved (+ (* dir move-amount) pos) pos)
               (* -1.0 dir)
               (if (= -1.0 dir) (* 0.5 move-amount) move-amount))))))

(let [s (str (encode 32.19 90 0.001))]
  (println (str "Encoded:  " s))
  (println (str "Num bits: " (count s)))
  (println (str "Hex len:  " (/ (count s) 4)))
  (println (str "Encoded num: " (bin-str-to-num s))))
