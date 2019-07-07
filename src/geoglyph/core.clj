(ns geoglyph.core)

(defn bin-str-to-num
  [bin]
  (let [values (->> (map-indexed vector (reverse bin))
                    (map #(if (= (second %) \1) (Math/pow 2 (first %)) 0))
                    (reduce +)
                    (int))]
    values))

(defn num-to-hex
  [num]
  (let [digits (into {} (map-indexed vector "0123456789ABCDEF"))]
    (get digits num)))

(defn bin-stream-to-hex
  [bin]
  (->> (partition 4 bin)
       (map #(apply str %))
       (map bin-str-to-num)
       (map num-to-hex)
       (map str)
       (reduce str)))

(defn encode
  [number max-val accuracy]
  (loop [encoded ""
         pos 0.0
         dir 1.0
         move-amount (* 0.5 max-val)]
    (println (prn-str [number max-val accuracy encoded (bin-stream-to-hex encoded) pos dir move-amount]))
    (if (or (<= 128 (count encoded)) (<= (Math/abs (- pos number)) accuracy))
      (if (empty? encoded)
        "00"
        encoded)
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

(let [s (str (encode 45 90 0.01))]
  (println (str "Encoded:  " s))
  (println (str "Num bits: " (count s)))
  (println (str "Hex len:  " (/ (count s) 4)))
  (println (str "Encoded num: " (bin-str-to-num s))))
