package com.example.marinobarbersalon.Cliente

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType


import androidx.navigation.navArgument
import com.example.marinobarbersalon.Cliente.Account.Account
import com.example.marinobarbersalon.Cliente.Account.DatiPersonali
import com.example.marinobarbersalon.Cliente.Account.InserisciRecensione
import com.example.marinobarbersalon.Cliente.Account.ListaRecensioniViewModel
import com.example.marinobarbersalon.Cliente.Account.NotificheClienteViewModel
import com.example.marinobarbersalon.Cliente.Account.Prenotazioni
import com.example.marinobarbersalon.Cliente.Account.Recensioni
import com.example.marinobarbersalon.Cliente.Home.HomeScreen
import com.example.marinobarbersalon.Cliente.Home.ListaServiziViewModel
import com.example.marinobarbersalon.Cliente.Home.Riepilogo
import com.example.marinobarbersalon.Cliente.Home.SelezionaGiorno
import com.example.marinobarbersalon.Cliente.Home.SelezionaServiziobarba
import com.example.marinobarbersalon.Cliente.Home.SelezioneServizioCapelli
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.Cliente.ScaffoldItems.TopBarMia
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigation(modifier: Modifier, navController : NavHostController, userViewModel : UserViewModel, notificheClienteViewModel: NotificheClienteViewModel, logout : () -> Unit) {

    val listaServiziViewModel : ListaServiziViewModel = viewModel()
    val listaRecensioniViewModel : ListaRecensioniViewModel = viewModel()

    NavHost(navController, startDestination = "home"){



        //clienteNavGraph(modifier = modifier, navController, userViewModel, listaServiziViewModel, logout)
        //adminNavGraph(navController, adminViewModel)

        composable(Screen.Home.route) {
            val userState by userViewModel.userState.collectAsState()

            if (userState.nome.isNullOrEmpty() ) {
                // Mostra una schermata di caricamento o uno stato intermedio
                Box(
                    modifier = modifier.fillMaxSize().background(my_grey),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = my_gold) // Indicatore di caricamento
                }
            }else{
                Scaffold(
                    modifier = modifier,
                    containerColor = my_grey,
                    topBar = {
                        TopBarMia(
                            titolo = "BENVENUTO " + userState.nome!!.uppercase(),
                            showIcon = false,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    //bottomBar = {}
                ) { padding ->
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        thickness = 2.dp,
                        color = my_gold
                    )
                    HomeScreen(
                        modifier = Modifier.padding(padding),
                        onNavigateToLogin = {
                            //navController.navigate(Screen.Login.route)
                            logout()
                        },
                        onNavigateToSelezionaServizioBarba = {
                            navController.navigate(Screen.SelezionaServizioBarba.route)
                        },
                        onNavigateToSelezionaServizioCapelli = {
                            navController.navigate(Screen.SelezionaServizioCapelli.route)
                        },
                        userViewModel = userViewModel)
                }
            }


        }
        composable(Screen.SelezionaServizioCapelli.route) {

            Scaffold(
                modifier = modifier,
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
                //bottomBar = { BarraNavigazione(navController) }
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
                modifier = modifier,
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
                //bottomBar = { BarraNavigazione(navController) }
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
        composable(
            Screen.SelezionaGiorno.route + "/{idSer}",
            arguments =  listOf(
                navArgument(name = "idSer"){
                    type = NavType.StringType
                }
            )
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("idSer")

            Scaffold(
                modifier = modifier,
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
                //bottomBar = { BarraNavigazione(navController) }
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

        composable(
            Screen.Riepilogo.route + "/{idSer}/{orarioInizio}/{orarioFine}/{dataSelezionata}",
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
                modifier = modifier,
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
                //bottomBar = { BarraNavigazione(navController) }
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
                modifier = modifier,
                containerColor = my_grey,
                topBar = {
                    TopBarMia(
                        titolo = "ACCOUNT",
                        showIcon = false,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                },
                //bottomBar = { BarraNavigazione(navController) }
            ) { padding ->
                Account(
                    modifier = Modifier.padding(padding),
                    userViewModel = userViewModel,
                    notificheClienteViewModel = notificheClienteViewModel,
                    onNavigaDatiPersonali = {
                        navController.navigate(Screen.DatiPersonali.route)
                    },
                    onNavigaPrenotazioni = {
                        navController.navigate(Screen.Prenotazioni.route)
                    },
                    onNavigaRecensioni = {
                        navController.navigate(Screen.Recensioni.route)
                    }
                )
            }


        }

        composable(Screen.DatiPersonali.route){
            Scaffold(
                modifier = modifier,
                containerColor = my_grey,
                topBar = {
                    TopBarMia(
                        titolo = "DATI PERSONALI",
                        showIcon = true,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            ) { paddingValues ->
                DatiPersonali(
                    modifier = Modifier.padding(paddingValues),
                    userViewModel = userViewModel
                )
            }
        }

        composable(Screen.Prenotazioni.route){
            Scaffold(
                modifier = modifier,
                containerColor = my_grey,
                topBar = {
                    TopBarMia(
                        titolo = "PRENOTAZIONI",
                        showIcon = true,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            ) { paddingValues ->
                Prenotazioni(
                    modifier = Modifier.padding(paddingValues),
                    userViewModel = userViewModel
                )
            }
        }

        composable(Screen.Recensioni.route){
            Scaffold(
                modifier = modifier,
                containerColor = my_grey,
                topBar = {
                    TopBarMia(
                        titolo = "RECENSIONI",
                        showIcon = true,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            ) { paddingValues ->
                Recensioni(
                    modifier = Modifier.padding(paddingValues),
                    listaRecensioniViewModel = listaRecensioniViewModel,
                    onNavigateToInserisciRecensione = {
                        navController.navigate(Screen.InserisciRecensione.route)
                    }
                )
            }
        }

        composable(Screen.InserisciRecensione.route){
            Scaffold(
                modifier = modifier,
                containerColor = my_grey,
                topBar = {
                    TopBarMia(
                        titolo = "INSERISCI RECENSIONE",
                        showIcon = true,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            ) { paddingValues ->
                InserisciRecensione(
                    modifier = Modifier.padding(paddingValues),
                    listaRecensioniViewModel = listaRecensioniViewModel
                )
            }
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
    object Account : Screen("account")
    object DatiPersonali : Screen("datiPersonali")
    object Prenotazioni : Screen("prenotazioni")
    object Recensioni : Screen("recensioni")
    object InserisciRecensione : Screen("inserisciRecensione")
    object Shop : Screen("shop")
    object Impostazioni : Screen("impostazioni")

    ///////////////////////////////////////////////////////
    object HomeAdmin : Screen("homeAdmin")
    object Prova : Screen("prova")
    object Provadue : Screen("provadue")
    object CalendarScreen : Screen("calendarScreen")
    object NextScreen : Screen("nextScreen/{date}")

}