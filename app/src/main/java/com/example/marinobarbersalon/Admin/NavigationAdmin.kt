package com.example.marinobarbersalon.Admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti.VisualizzaAppuntamenti
import com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti.VisualizzaAppuntamenti1
import com.example.marinobarbersalon.Cliente.Screen

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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAdmin(modifier: Modifier, navController : NavHostController, adminViewModel : AdminViewModel, logout : () -> Unit) {

    NavHost(navController, startDestination = "homeAdmin"){

        composable(Screen.HomeAdmin.route){
            HomeAdmin(
                modifier = modifier,
                adminViewModel = adminViewModel,
                onNavigateToLogin = {
                    logout()
                }
            )
        }

        composable(Screen.Prova.route){
            Prova(modifier = modifier,
                clicca = {
                    navController.navigate(Screen.Provadue.route)
                })
        }

        composable(Screen.Provadue.route){

            Provadue(modifier = modifier)

        }

        composable(Screen.VisualizzaAppuntamenti.route) {
            VisualizzaAppuntamenti(
                onNavigateToNextPage = { date ->
                    navController.navigate("visualizzaAppuntamenti1/$date")
                }
            )
        }

        composable(Screen.VisualizzaAppuntamenti1.route) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date")
            if (date != null) {
                VisualizzaAppuntamenti1(date = date)
            } else {
                Text(text = "Data non disponibile.")
            }
        }



    }


}