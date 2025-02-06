package com.example.marinobarbersalon.Cliente.ScaffoldItems

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marinobarbersalon.Cliente.Account.NotificheClienteViewModel
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMia(
    titolo : String = "prova",
    showIcon : Boolean = true,
    onBack : () -> Unit = {}
){
    Column(
        Modifier.fillMaxWidth()
    ) {
        CenterAlignedTopAppBar(
            title = {

                Text(
                    text = titolo,
                    fontSize = 25.sp,
                    color = my_white,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = if (!showIcon) 0.dp else (-24).dp),
                )


            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = my_grey
            ),
            navigationIcon = {
                if (showIcon){
                    IconButton(onClick = {onBack()},
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
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


@Composable
fun BarraNavigazione(navController : NavController, notificheClienteViewModel: NotificheClienteViewModel) {

    val notificheProdotti by notificheClienteViewModel.notificheProdotti.collectAsState()
    val notifichePrenotazioni by notificheClienteViewModel.notifichePrenotazioni.collectAsState()

    val notifiche = notifichePrenotazioni + notificheProdotti

    val navItemList = listOf(
        NavItem(Screen.Home.route, ImageVector.vectorResource(R.drawable.sharp_content_cut_24)),
        NavItem(Screen.Account.route, Icons.Outlined.AccountCircle),
        NavItem(Screen.SelezionaShop.route, Icons.Outlined.ShoppingCart),
    )
    val rottaCorrente = navController.currentBackStackEntryAsState().value?.destination?.route


    //mapping della rotta per evidenziare l'icona della bottomBar
    val rottaDaEvidenziare = when (rottaCorrente) {
        Screen.SelezionaServizioBarba.route -> Screen.Home.route
        Screen.SelezionaServizioCapelli.route -> Screen.Home.route
        Screen.SelezionaGiorno.route + "/{idSer}" -> Screen.Home.route
        Screen.Riepilogo.route + "/{idSer}/{orarioInizio}/{orarioFine}/{dataSelezionata}" -> Screen.Home.route
        Screen.DatiPersonali.route -> Screen.Account.route
        Screen.Prenotazioni.route -> Screen.Account.route
        Screen.ProdottiPrenotati.route -> Screen.Account.route
        Screen.Recensioni.route -> Screen.Account.route
        Screen.InserisciRecensione.route -> Screen.Account.route
        Screen.Shop.route + "/{categoria}" -> Screen.SelezionaShop.route
        Screen.ProdottoShop.route+ "/{nomeProdotto}" -> Screen.SelezionaShop.route
        else -> rottaCorrente
    }
    Log.d("NAVBAR", rottaDaEvidenziare.toString())

    Box{

        NavigationBar(containerColor = Color(0xFF333333)) {
            navItemList.forEachIndexed { index, navItem ->
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        if(rottaDaEvidenziare != navItem.route){
                            navController.navigate(navItem.route){
                                //mi salva lo stato e non mi duplica le schermate
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {

                        BadgedBox(badge = {
                            if (navItem.route == Screen.Account.route && notifiche>0){
                                Badge(containerColor = my_bordeaux){
                                    Text(
                                        text = notifiche.toString(),
                                        color = my_gold,

                                    )
                                }
                            }
                        }) {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon",
                                tint = if (rottaDaEvidenziare != navItem.route){
                                    my_white
                                }else{
                                    my_bordeaux
                                },
                                modifier = Modifier.size(45.dp)
                            )
                        }

                    }
                )
            }

        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp,
            color = my_white
        )
    }



}