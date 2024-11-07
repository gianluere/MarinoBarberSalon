package com.example.marinobarbersalon.Cliente.Account

import com.google.firebase.firestore.DocumentReference

data class Appuntamento(
    val titolo : String = "",
    val descrizione : String = "",
    val cliente : DocumentReference? = null,
    val orarioInizio : String = "",
    val orarioFine : String = "",
    val data : String = ""
)
