package com.example.marinobarbersalon.Admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scope: CoroutineScope, drawerState: DrawerState) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF333333), //background color
            titleContentColor = Color.White //content color
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarDrawer(showDrawer : Boolean, navController: NavController, onClickDrawer : () -> Unit) {

    val rottaCorrente = navController.currentBackStackEntryAsState().value?.destination?.route

    val titolo = when (rottaCorrente) {
        Screen.HomeAdmin.route -> "VISUALIZZA APPUNTAMENTI"
        Screen.Prova.route -> "PROVA"
        Screen.Provadue.route -> "PROVADue"
        Screen.VisualizzaAppuntamenti.route -> "VISUALIZZA APPUNTAMENTI"
        Screen.VisualizzaAppuntamenti1.route -> "VISUALIZZA APPUNTAMENTI"
        Screen.VisualizzaClienti.route -> "VISUALIZZA CLIENTI"
        Screen.DettagliCliente.route -> "VISUALIZZA CLIENTI"
        Screen.VisualizzaServizi.route -> "VISUALIZZA SERVIZI"
        Screen.AggiungiServizio.route -> "VISUALIZZA SERVIZI"
        Screen.VisualizzaProdotti.route -> "VISUALIZZA PRODOTTI"
        Screen.VisualizzaProdottiDettaglio.route -> "VISUALIZZA PRODOTTI"
        Screen.AggiungiProdotto.route -> "VISUALIZZA PRODOTTI"
        Screen.Recensioni.route -> "VISUALIZZA RECENSIONI"
        Screen.StatsBase.route -> "STATISTICHE"
        Screen.VisualizzaStatisticheAppuntamenti.route -> "STATISTICHE"
        Screen.VisualizzaStatisticheClienti.route -> "STATISTICHE"
        Screen.VisualizzaServiziPiuRichiesti.route -> "STATISTICHE"
        Screen.VisualizzaEntrateMensili.route -> "STATISTICHE"
        Screen.VisualizzaProdottiPrenotati.route -> "PRODOTTI PRENOTATI"
        else -> "Default"
    }

Column {
    TopAppBar(
        title = {

            Text(
                text = titolo,
                fontSize = 25.sp,
                color = my_white,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            )


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = my_grey
        ),
        navigationIcon = {
            if (showDrawer) {
                IconButton(
                    onClick = { onClickDrawer() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu, contentDescription = "menu",
                        tint = my_white
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
                        tint = my_white,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }

        }

    )
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = 2.dp,
        color = my_gold
    )
}



}



/*
//definisce come verranno visualizzati gli elementi del drawer
@Composable
fun DrawerItem(item: NavDrawerItem, selected: Boolean, onItemClick: (NavDrawerItem) -> Unit) {
    val background = if (selected) Color(0xFF333333) else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(background)
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

 */








/*
sealed class NavDrawerItem(var route: String, var icon: Int, var title: String){
    object HomeAdmin : NavDrawerItem("homeAdmin", R.drawable.sharp_content_cut_24, "Home")

}

 */