package com.example.marinobarbersalon

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController


import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType


import androidx.navigation.navArgument
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigation(modifier: Modifier, navController : NavHostController, userViewModel : UserViewModel, logout : () -> Unit) {

    val listaServiziViewModel : ListaServiziViewModel = viewModel()
    NavHost(navController, startDestination = "clientGraph"){
        /*
        composable(Screen.Login.route){
            LoginScreen(navController, userViewModel, adminViewModel)
        }

         */

        clienteNavGraph(modifier = modifier, navController, userViewModel, listaServiziViewModel, logout)
        //adminNavGraph(navController, adminViewModel)

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
    object Shop : Screen("shop")
    object Impostazioni : Screen("impostazioni")

    ///////////////////////////////////////////////////////
    object HomeAdmin : Screen("homeAdmin")

}