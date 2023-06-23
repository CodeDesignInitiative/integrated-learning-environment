(ns ile.ui.legal.view)


(defn privacy-statement [_request]
  [:main
   [:h1 "Datenschutzerklärung"]

   [:h2 "Ansprechpartner"]

   [:p "paul.hempel@code.design"]

   [:p "Diese Webseite benutzt kein Tracking."]
   [:p "Es werden nur notwendige Session Cookies gesetzt."]
   [:p "Nutzerkonten können auf Anfrage an den Ansprechpartner gelöscht werden.
   Sie beinhalten die Daten die von den Nutzenden eingegeben wurden:
   Email, Passworthash, gespeicherter Fortschritt und Programme."]])