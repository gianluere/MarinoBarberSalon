package com.example.marinobarbersalon.Cliente.Home

data class Servizio(
    var id: String = "", //mi serve in aggiungi servizi
    var nome : String? = null,
    var descrizione : String? = null,
    val tipo : String? = null,
    val durata : Int? = 0,
    val prezzo : Double = 0.00
)