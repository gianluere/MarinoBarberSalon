package com.example.marinobarbersalon

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController


import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavType


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marinobarbersalon.ui.theme.my_grey

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(userViewModel : UserViewModel, adminViewModel: AdminViewModel) {

    val listaServiziViewModel : ListaServiziViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Login.route){
        composable(Screen.Login.route){
            LoginScreen(navController, userViewModel, adminViewModel)
        }
        navigation(
            route = "clientGraph",
            startDestination = Screen.Home.route
        ) {
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

                Scaffold(
                    containerColor = my_grey,
                    topBar = {
                        TopBarMia(
                            titolo = "Prenota un appuntamento",
                            showIcon = true,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    bottomBar = { BarraNavigazione(navController) }
                ) { padding ->
                    SelezioneServizioCapelli(
                        modifier = Modifier.padding(padding),
                        viewModel = listaServiziViewModel,
                        onNavigateToSelezionaGiorno = {idSer ->
                            navController.navigate(Screen.SelezionaGiorno.route + "/$idSer")
                        })
                }


            }
            composable(Screen.SelezionaServizioBarba.route) {
                Scaffold(
                    containerColor = my_grey,
                    topBar = {
                        TopBarMia(
                            titolo = "Prenota un appuntamento",
                            showIcon = true,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    bottomBar = { BarraNavigazione(navController) }
                ){padding ->
                    SelezionaServiziobarba(
                        modifier = Modifier.padding(padding),
                        viewModel = listaServiziViewModel,
                        onNavigateToSelezionaGiorno = {idSer ->
                            navController.navigate(Screen.SelezionaGiorno.route + "/$idSer")
                        }
                    )
                }

            }
            composable(Screen.SelezionaGiorno.route + "/{idSer}",
                arguments =  listOf(
                    navArgument(name = "idSer"){
                        type = NavType.StringType
                    }
                )
            ){ backStackEntry ->
                val id = backStackEntry.arguments?.getString("idSer")

                Scaffold(
                    containerColor = my_grey,
                    topBar = {
                        TopBarMia(
                            titolo = "Scegli un giorno e un orario",
                            showIcon = true,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    bottomBar = { BarraNavigazione(navController) }
                ){padding ->
                    SelezionaGiorno(
                        modifier = Modifier.padding(padding),
                        listaServiziViewModel = listaServiziViewModel,
                        idSer = id.toString(),
                        onNavigateToRiepilogo = {idSer, orarioInizio, orarioFine, dataSelezionata->
                            navController.navigate(Screen.Riepilogo.route + "/$idSer" + "/$orarioInizio" +"/$orarioFine" + "/$dataSelezionata")
                        }
                    )
                }


            }

            composable(Screen.Riepilogo.route + "/{idSer}/{orarioInizio}/{orarioFine}/{dataSelezionata}",
                arguments =  listOf(
                    navArgument(name = "idSer"){
                        type = NavType.StringType
                    },
                    navArgument(name = "orarioInizio"){
                        type = NavType.StringType
                    },
                    navArgument(name = "orarioFine"){
                        type = NavType.StringType
                    },
                    navArgument(name = "dataSelezionata"){
                        type = NavType.StringType
                    }
                )){backStackEntry ->
                val id = backStackEntry.arguments?.getString("idSer")
                val orarioInizio = backStackEntry.arguments?.getString("orarioInizio")
                val orarioFine = backStackEntry.arguments?.getString("orarioFine")
                val dataSelezionata = backStackEntry.arguments?.getString("dataSelezionata")

                Scaffold(
                    containerColor = my_grey,
                    topBar = {
                        TopBarMia(
                            titolo = "Riepilogo",
                            showIcon = true,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    bottomBar = { BarraNavigazione(navController) }
                ){padding ->
                    Riepilogo(
                        modifier = Modifier.padding(padding),
                        userViewModel = userViewModel,
                        listaServiziViewModel = listaServiziViewModel,
                        idSer = id.toString(),
                        orarioInizio = orarioInizio.toString(),
                        orarioFine = orarioFine.toString(),
                        dataSelezionata = dataSelezionata.toString(),
                        onSuccess = {
                            navController.popBackStack(route = Screen.Home.route, inclusive = false)
                        }
                    )
                }



            }

            composable(Screen.Account.route){
                Scaffold(
                    containerColor = my_grey,
                    topBar = {
                        TopBarMia(
                            titolo = "Account",
                            showIcon = false,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    bottomBar = { BarraNavigazione(navController) }
                ) { padding ->
                    Account(modifier = Modifier.padding(padding), userViewModel)
                }


            }
        }
        adminNavGraph(navController, adminViewModel)

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
    object Account : Screen("account")
    object Shop : Screen("shop")
    object Impostazioni : Screen("impostazioni")

    ///////////////////////////////////////////////////////
    object HomeAdmin : Screen("homeAdmin")

}