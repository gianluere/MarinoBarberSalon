package com.example.marinobarbersalon

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.adminNavGraph(navController: NavController, adminViewModel: AdminViewModel) {
    navigation(
        route = "adminGraph",
        startDestination = "prova"
    ) {
        composable("prova"){
            Prova(adminViewModel = adminViewModel)
        }
    }
}