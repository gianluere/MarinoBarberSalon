package com.example.marinobarbersalon.Admin

data class Admin(
    var state : AuthState? = null,
    val nome : String? = null,
    val cognome : String? = null,
    val email : String? = null,
    val eta : Int? = 0,
    val password : String? = null,
    val telefono : String? = null
)

sealed class AuthState(){
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}


