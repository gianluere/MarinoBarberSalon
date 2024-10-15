package com.example.marinobarbersalon

import com.google.firebase.firestore.DocumentReference

data class User(
    var state : AuthState? = null,
    val nome : String? = null,
    val cognome : String? = null,
    val email : String? = null,
    val eta : Int? = 0,
    val password : String? = null,
    val telefono : String? = null,
    val appuntamenti: List<DocumentReference> = emptyList()
)

sealed class AuthState(){
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

