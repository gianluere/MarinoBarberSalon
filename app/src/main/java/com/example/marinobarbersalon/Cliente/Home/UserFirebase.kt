package com.example.marinobarbersalon.Cliente.Home

import com.google.firebase.firestore.DocumentReference


data class UserFirebase(
    val nome : String = "",
    val cognome : String = "",
    val email : String = "",
    val eta : Int = 0,
    val telefono : String = "",
    val appuntamenti: List<DocumentReference> = emptyList()
){
    //serve in VisualizzaClientiVM per la ricerca
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$nome$cognome",
            "$nome $cognome",
            "${nome.first()} ${cognome.first()}",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

