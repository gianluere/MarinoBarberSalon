package com.example.marinobarbersalon

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationAdmin(adminNavController: NavHostController, userViewModel: UserViewModel, adminViewModel: AdminViewModel) {
    NavHost(navController = adminNavController, startDestination = ScreenAdmin.Prova.route) {
        composable(ScreenAdmin.Prova.route) {
            Prova() // La tua schermata Prova
        }

        // Aggiungi qui altre schermate per l'amministratore
    }
}

sealed class ScreenAdmin(val route: String) {
    object Prova : ScreenAdmin("prova")
}
