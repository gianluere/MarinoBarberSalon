package com.example.marinobarbersalon

import com.google.firebase.firestore.DocumentReference

data class Appuntamento(
    val servizio : DocumentReference? = null,
    val cliente : DocumentReference? = null,
    val orarioInizio : String = "",
    val orarioFine : String = "",
    val data : String = ""
)
