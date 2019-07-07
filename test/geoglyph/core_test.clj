(ns geoglyph.core-test
  (:require [clojure.test :refer :all]
            [geoglyph.core :refer :all]))

(deftest test-bin-to-num
  (testing "Binary string to number conversion"
    (is (= (bin-str-to-num "110") 6))))

(deftest test-encode
  (testing "Geoglyph encoding to binary"
    (and (is (= (encode 32.67 90.0 0.01)
                "10011000000100100000011"))
         (is (= (encode -96.85 180 0.02)
                "01000001100001001001")))))
