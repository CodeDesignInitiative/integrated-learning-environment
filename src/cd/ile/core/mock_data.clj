(ns cd.ile.core.mock-data
  (:import (java.time LocalDate)))


(def notes ["Chat öffnen"
            "Code Editor reinschauen"
            "Aufträge checken"])

(def notifications [{:date    (LocalDate/of 2023 2 13)
                     :heading "Willkommen im Team!"
                     :msg     "Damit du hier einen guten Start hast, kümmert sich Xara um dich. Sie sollte dir schon geschrieben haben. Schau mal in den Chat.\nIch habe es dir auch als Erinnerung auf deinen digitalen Notizzettel geschrieben."}
                    {:date    (LocalDate/of 2023 2 21)
                     :heading "Du hast einen neuen Auftrag!"
                     :msg     "Schau in deine Aufträge links und sieh nach was es so gibt."}
                    {:date    (LocalDate/of 2023 2 24)
                     :heading "Danke für deine Hilfe bei dem letzten Projekt."
                     :msg     "..."}])

