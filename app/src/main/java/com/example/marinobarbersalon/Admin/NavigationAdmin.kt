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

        composable(Screen.VisualizzaClienti.route) {
            VisualizzaClienti(
                onNavigateToDetails = { clienteEmail ->
                    navController.navigate("dettagliCliente/${clienteEmail}")
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

        composable(Screen.VisualizzaServizi.route){
            VisualizzaServizi(
                onNavigateToAddServizio = {
                    navController.navigate(Screen.AggiungiServizio.route)
                }
            )
        }

        composable(Screen.AggiungiServizio.route){
            AggiungiServizio(
                onAggiungiSuccess = { navController.popBackStack() },
                onAnnullaClick = { navController.popBackStack() }
            )
        }

        composable(Screen.VisualizzaProdotti.route){
            VisualizzaProdotti(
                onNavigateToNextPage = { categoria ->
                    navController.navigate("visualizzaProdottiDettaglio/$categoria")
                }
            )
        }
        composable(Screen.VisualizzaProdottiDettaglio.route) {backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria")

            if (categoria != null) {
                VisualizzaProdottiDettaglio(
                    categoria = categoria,
                    onNavigateToAddProdotto = {
                        navController.navigate("aggiungiProdotto/$categoria")
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
                modifier = Modifier.padding(top = 64.dp),
                listaRecensioniViewModel = listaRecensioniViewModel,
                isAdmin = true,
                onNavigateToInserisciRecensione = {
                    navController.navigate(Screen.InserisciRecensione.route)
                }
            )
        }










    }


}