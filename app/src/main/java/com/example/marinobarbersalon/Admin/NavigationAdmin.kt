package com.example.marinobarbersalon.Admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.marinobarbersalon.Admin.Servizi.AggiungiServizio
import com.example.marinobarbersalon.Admin.Servizi.VisualizzaServizi
import com.example.marinobarbersalon.Admin.Stats.VisualizzaStatistiche
import com.example.marinobarbersalon.Admin.Stats.VisualizzaStatisticheAppuntamenti
import com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti.VisualizzaAppuntamenti
import com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti.VisualizzaAppuntamenti1
import com.example.marinobarbersalon.Admin.VisualizzaClienti.DettagliCliente
import com.example.marinobarbersalon.Admin.VisualizzaClienti.VisualizzaClienti
import com.example.marinobarbersalon.Admin.VisualizzaProdotti.AggiungiProdotto
import com.example.marinobarbersalon.Admin.VisualizzaProdotti.VisualizzaProdotti
import com.example.marinobarbersalon.Admin.VisualizzaProdotti.VisualizzaProdottiDettaglio
import com.example.marinobarbersalon.Cliente.Account.ListaRecensioniViewModel
import com.example.marinobarbersalon.Cliente.Account.Recensioni
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

    val listaRecensioniViewModel : ListaRecensioniViewModel = viewModel()

    /* I caratteri "\\ fungono da escape character
       La Regexp va a sostituire nella stringa di partenza
       l'argomento passato togliendo le graffe
       (Prima barra (\): serve da escape per la seconda barra nella stringa
        dato che (\) è anche esso un carattere speciale nelle stringhe di Kotlin.
    */

    fun String.withArg(arg: String): String {
        return this.replace("\\{.*?\\}".toRegex(), arg)
    }


    NavHost(navController, startDestination = "homeAdmin"){

        composable(Screen.HomeAdmin.route) {
            HomeAdmin(
                modifier = modifier,
                adminViewModel = adminViewModel,
                onNavigateToLogin = {
                    logout()
                }
            )
        }

        composable(Screen.Prova.route) {
            Prova(
                modifier = modifier,
                clicca = {
                    navController.navigate(Screen.Provadue.route)
                }
            )
        }

        composable(Screen.Provadue.route) {
            Provadue(modifier = modifier)
        }

        composable(Screen.VisualizzaAppuntamenti.route) {
            VisualizzaAppuntamenti(
                onNavigateToNextPage = { date ->
                    navController.navigate(Screen.VisualizzaAppuntamenti1.route.withArg(date))
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

        composable(Screen.VisualizzaClienti.route) {
            VisualizzaClienti(
                onNavigateToDetails = { clienteEmail ->
                    navController.navigate(Screen.DettagliCliente.route.withArg(clienteEmail))
                }
            )
        }

        composable(Screen.DettagliCliente.route) { backStackEntry ->
            val clienteEmail = backStackEntry.arguments?.getString("clienteEmail")
            if (clienteEmail != null) {
                DettagliCliente(clienteEmail)
            } else {
                Text(text = "NESSUNA EMAIL PASSATA")
            }
        }

        composable(Screen.VisualizzaServizi.route) {
            VisualizzaServizi(
                onNavigateToAddServizio = {
                    navController.navigate(Screen.AggiungiServizio.route)
                }
            )
        }

        composable(Screen.AggiungiServizio.route) {
            AggiungiServizio(
                onAggiungiSuccess = { navController.popBackStack() },
                onAnnullaClick = { navController.popBackStack() }
            )
        }

        composable(Screen.VisualizzaProdotti.route) {
            VisualizzaProdotti(
                onNavigateToNextPage = { categoria ->
                    navController.navigate(Screen.VisualizzaProdottiDettaglio.route.withArg(categoria))
                }
            )
        }

        composable(Screen.VisualizzaProdottiDettaglio.route) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria")
            if (categoria != null) {
                VisualizzaProdottiDettaglio(
                    categoria = categoria,
                    onNavigateToAddProdotto = {
                        navController.navigate(Screen.AggiungiProdotto.route.withArg(categoria))
                    }
                )
            } else {
                Text(text = "NESSUNA CATEGORIA PASSATA")
            }
        }

        composable(Screen.AggiungiProdotto.route) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria")
            if (categoria != null) {
                AggiungiProdotto(
                    categoria = categoria,
                    onAggiungiSuccess = { navController.popBackStack() },
                    onAnnullaClick = { navController.popBackStack() },
                )
            } else {
                Text(text = "NESSUNA CATEGORIA PASSATA")
            }
        }

        composable(Screen.Recensioni.route) {
            Recensioni(
                modifier = Modifier.padding(top = 100.dp),
                listaRecensioniViewModel = listaRecensioniViewModel,
                isAdmin = true,
                onNavigateToInserisciRecensione = {
                    navController.navigate(Screen.InserisciRecensione.route)
                }
            )
        }

        composable(Screen.StatsBase.route){
            VisualizzaStatistiche(
                onNavigateToVisualizzaStatisticheAppuntamenti = {
                    navController.navigate(Screen.VisualizzaStatisticheAppuntamenti.route)
                }
            )
        }

        composable(Screen.VisualizzaStatisticheAppuntamenti.route){
            VisualizzaStatisticheAppuntamenti()
        }
















    }


}