package com.example.marinobarbersalon

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(userViewModel : UserViewModel, adminViewModel: AdminViewModel) {
    val navController = rememberNavController()
    val adminNavController = rememberNavController()
    NavigationAdmin(adminNavController, userViewModel, adminViewModel)
    NavHost(navController, startDestination = Screen.Login.route){
        composable(Screen.Login.route){
            LoginScreen(navController, adminNavController,userViewModel, adminViewModel)
        }
        composable(Screen.SignUp.route){
            SignUpScreen(navController, userViewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToSelezionaServizioBarba = {
                    navController.navigate(Screen.SelezionaServizioBarba.route)
                },
                onNavigateToSelezionaServizioCapelli = {
                    navController.navigate(Screen.SelezionaServizioCapelli.route)
                },
                userViewModel)
        }
        composable(Screen.SelezionaServizioCapelli.route) {
            SelezioneServizioCapelli(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToSelezionaGiorno = {
                    navController.navigate(Screen.SelezionaGiorno.route)
                })
        }
        composable(Screen.SelezionaServizioBarba.route) {
            SelezionaServiziobarba(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToSelezionaGiorno = {
                    navController.navigate(Screen.SelezionaGiorno.route)
                }
            )
        }
        composable(Screen.SelezionaGiorno.route) {
            SelezionaGiorno(onBack = {
                navController.popBackStack()
            })
        }

    }

}

sealed class Screen(val route:String ){
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object SelezionaServizioCapelli : Screen("selezionaServizioCapelli")
    object SelezionaServizioBarba : Screen("selezionaServizioBarba")
    object SelezionaGiorno : Screen("selezionaGiorno")
}