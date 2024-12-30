package com.example.marinobarbersalon.Admin

import com.example.marinobarbersalon.Cliente.Home.AuthState

data class Admin(
    var state : AuthState = AuthState.None,
    val nome : String? = null,
    val cognome : String? = null,
    val email : String? = null,
    val eta : Int? = 0,
    val telefono : String? = null
)



