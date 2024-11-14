package com.example.marinobarbersalon.Cliente.Account

import com.google.firebase.firestore.DocumentReference

data class Appuntamento(
    val servizio : String = "",
    var descrizione : String = "",
    val prezzo : Double = 0.00,
    val cliente : DocumentReference? = null,
    val orarioInizio : String = "",
    val orarioFine : String = "",
    val data : String = ""
)
