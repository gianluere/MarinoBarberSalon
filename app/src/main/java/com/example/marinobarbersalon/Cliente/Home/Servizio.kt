package com.example.marinobarbersalon.Cliente.Home

data class Servizio(
    var nome : String? = null,
    var descrizione : String? = null,
    val tipo : String? = null,
    val durata : Int? = 0,
    val prezzo : Double = 0.00
)