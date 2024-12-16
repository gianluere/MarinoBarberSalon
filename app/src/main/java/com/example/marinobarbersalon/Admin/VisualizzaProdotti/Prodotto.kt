package com.example.marinobarbersalon.Admin.VisualizzaProdotti

data class Prodotto(
    val id : String = "",
    val nome : String = "",
    val descrizione : String = "",
    val prezzo : Double = 0.0,
    val quantita : Int = 0,
    val immagine : String = "",
    val categoria : String = ""
)
