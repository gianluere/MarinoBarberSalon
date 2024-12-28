package com.example.marinobarbersalon.Cliente.Shopping

import com.google.firebase.firestore.DocumentReference

data class ProdottoPrenotato(
    val prodotto : DocumentReference? = null,
    val quantita : Int = 0,
    val stato : String = "",
    val utente : DocumentReference? = null,
    val data : String = ""
)
