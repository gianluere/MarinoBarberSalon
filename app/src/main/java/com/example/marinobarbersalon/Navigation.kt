package com.example.marinobarbersalon

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavType


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(userViewModel : UserViewModel) {

    val listaServiziViewModel: ListaServiziViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Login.route){
        composable(Screen.Login.route){
            LoginScreen(navController, userViewModel)
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
                viewModel = listaServiziViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToSelezionaGiorno = {idSer ->
                    navController.navigate(Screen.SelezionaGiorno.route + "/$idSer")
                })
        }
        composable(Screen.SelezionaServizioBarba.route) {
            SelezionaServiziobarba(
                viewModel = listaServiziViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToSelezionaGiorno = {idSer ->
                    navController.navigate(Screen.SelezionaGiorno.route + "/$idSer")
                }
            )
        }
        composable(Screen.SelezionaGiorno.route + "/{idSer}",
            arguments =  listOf(
                navArgument(name = "idSer"){
                    type = NavType.StringType
                }
            )
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("idSer")
            SelezionaGiorno(
                listaServiziViewModel = listaServiziViewModel,
                onBack = {
                    navController.popBackStack()
                },
                idSer = id.toString(),
                onNavigateToRiepilogo = {idSer->
                    navController.navigate(Screen.Riepilogo.route + "/$idSer")
                }
            )
        }
        /*{
            SelezionaGiorno(onBack = {
                navController.popBackStack()
            })
        }

         */

        composable(Screen.Riepilogo.route + "/{idSer}",
            arguments =  listOf(
                navArgument(name = "idSer"){
                    type = NavType.StringType
                }
            )){backStackEntry ->
            val id = backStackEntry.arguments?.getString("idSer")
            Riepilogo(
                userViewModel = userViewModel,
                listaServiziViewModel = listaServiziViewModel,
                idSer = id.toString(),
                onBack = {
                    navController.popBackStack()
                }
            )

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
    object Riepilogo : Screen("riepilogo")
}