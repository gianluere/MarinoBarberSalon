package com.example.marinobarbersalon.Cliente.Home

data class Servizio(
    var id: String = "", //mi serve in aggiungi servizi(N.B. io l'id non lo carico su firestore rimane solo LOCALE!)
    var nome : String? = null,
    var descrizione : String? = null,
    val tipo : String? = null,
    val durata : Int? = 0,
    val prezzo : Double = 0.00
)