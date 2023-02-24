(ns cd.ile.core.date-util
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)))

(def month-date-pattern "dd. MMM")
(defn format-date [d]
  (->> (DateTimeFormatter/ofPattern month-date-pattern)
       (.format d)))
(comment
  (format-date (LocalDate/now)))