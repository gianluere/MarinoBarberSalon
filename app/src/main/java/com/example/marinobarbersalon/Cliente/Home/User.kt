package com.example.marinobarbersalon.Cliente.Home

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

//classe di aiuto per l'autenticazione
sealed class AuthState(){
    object None : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

