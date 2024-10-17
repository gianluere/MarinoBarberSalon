package com.example.marinobarbersalon.Admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.marinobarbersalon.Screen

fun NavGraphBuilder.adminNavGraph(navController: NavController, adminViewModel: AdminViewModel) {
    navigation(
        route = "adminGraph",
        startDestination = Screen.HomeAdmin.route
    ) {
        composable(Screen.HomeAdmin.route){
            HomeAdmin(
                adminViewModel = adminViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo("adminGraph") { inclusive = true }
                        popUpTo("clientGraph") { inclusive = true }
                    }
                }
            )
        }
        composable(NavDrawerItem.HomeAdmin.route){
            HomeAdmin(
                adminViewModel = adminViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo("adminGraph") { inclusive = true }
                        popUpTo("clientGraph") { inclusive = true }
                    }
                }
            )
        }
    }
}