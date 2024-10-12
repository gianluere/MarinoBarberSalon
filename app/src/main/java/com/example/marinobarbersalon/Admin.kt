package com.example.marinobarbersalon

data class Admin(
    var state : AuthState? = null,
    val nome : String? = null,
    val cognome : String? = null,
    val email : String? = null,
    val eta : Int? = 0,
    val password : String? = null,
    val telefono : String? = null
)



