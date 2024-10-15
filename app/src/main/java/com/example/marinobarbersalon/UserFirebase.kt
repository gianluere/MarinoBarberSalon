package com.example.marinobarbersalon

import com.google.firebase.firestore.DocumentReference

data class UserFirebase(
    val nome : String = "",
    val cognome : String = "",
    val email : String = "",
    val eta : Int = 0,
    val telefono : String = "",
    val appuntamenti: List<DocumentReference> = emptyList()
)
