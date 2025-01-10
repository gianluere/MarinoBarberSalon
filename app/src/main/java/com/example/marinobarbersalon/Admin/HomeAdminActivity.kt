package com.example.marinobarbersalon.Admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.LocalMall

import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.marinobarbersalon.MainActivity
import com.example.marinobarbersalon.ui.theme.MarinoBarberSalonTheme
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_drawer
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import kotlinx.coroutines.launch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


class HomeAdminActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarinoBarberSalonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = Color(0xFF333333)
                    //color = MaterialTheme.colorScheme.background
                ) {
                    val adminViewModel : AdminViewModel = viewModel()
                    //val navController = rememberNavController()
                    //val adminState by adminViewModel.adminState.collectAsState()

                    //Parte da modificare


                    val items = listOf(
//                        NavDrawerItem(
//                            title = "Visualizza appuntamenti",
//                            route = Screen.HomeAdmin.route,
//                            selectedIcon = Icons.Filled.Home,
//                            unselectedIcon = Icons.Outlined.Home,
//                        ),
//                        NavDrawerItem(
//                            title = "Prova",
//                            route = Screen.Prova.route,
//                            selectedIcon = Icons.Filled.Person,
//                            unselectedIcon = Icons.Outlined.Person,
//                        ),
                        NavDrawerItem(
                            title = "Appuntamenti",
                            route = Screen.VisualizzaAppuntamenti.route,
                            selectedIcon = Icons.Filled.CalendarMonth,
                            unselectedIcon = Icons.Outlined.CalendarMonth,
                        ),
                        NavDrawerItem(
                            title = "Clienti",
                            route = Screen.VisualizzaClienti.route,
                            selectedIcon = Icons.Filled.Person,
                            unselectedIcon = Icons.Outlined.Person
                        ),
                        NavDrawerItem(
                            title = "Servizi",
                            route = Screen.VisualizzaServizi.route,
                            selectedIcon = Icons.Filled.ContentCut,
                            unselectedIcon = Icons.Outlined.ContentCut
                        ),
                        NavDrawerItem(
                            title = "Prodotti",
                            route = Screen.VisualizzaProdotti.route,
                            selectedIcon = Icons.Filled.ShoppingCart,
                            unselectedIcon = Icons.Outlined.ShoppingCart
                        ),
                        NavDrawerItem(
                            title = "Recensioni",
                            route = Screen.Recensioni.route,
                            selectedIcon = Icons.Filled.Grade,
                            unselectedIcon = Icons.Outlined.Grade
                        ),
                        NavDrawerItem(
                            title = "Statistiche",
                            route = Screen.StatsBase.route,
                            selectedIcon = Icons.Filled.QueryStats,
                            unselectedIcon = Icons.Outlined.QueryStats
                        ),
                        NavDrawerItem(
                            title = "Prodotti Prenotati",
                            route = Screen.VisualizzaProdottiPrenotati.route,
                            selectedIcon = Icons.Filled.LocalMall,
                            unselectedIcon = Icons.Outlined.LocalMall
                        )


                    )

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    /**
                     * INSERIRE QUI LE PAGINE CHE DEVONO AVERE IL DRAWER
                     * */
                    val pagineConDrawer = listOf(
                        Screen.HomeAdmin.route,
                        Screen.Prova.route,
                        Screen.VisualizzaAppuntamenti.route,
                        Screen.VisualizzaClienti.route,
                        Screen.VisualizzaServizi.route,
                        Screen.VisualizzaProdotti.route,
                        Screen.Recensioni.route,
                        Screen.StatsBase.route
                    )

                    val showDraweIcon = currentRoute in pagineConDrawer


                    ModalNavigationDrawer(
                        gesturesEnabled = drawerState.isOpen,
                        drawerContent = {
                            ModalDrawerSheet(
                                drawerContainerColor = my_drawer,
//                                drawerContentColor = my_white,

                            ) {
                                val scrollState = rememberScrollState()
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(scrollState)
                                ) {
                                    NavDrawerHeader()

                                    NavDrawerBody(
                                        items = items,
                                        currentRoute = currentRoute
                                    ) { currentNavigationItem ->

                                        navController.navigate(currentNavigationItem.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            navController.graph.startDestinationRoute?.let { startDestinationRoute ->
                                                // Pop up to the start destination, clearing the back stack
                                                popUpTo(startDestinationRoute) {
                                                    // Save the state of popped destinations
                                                    saveState = true
                                                }
                                            }

                                            // Configure navigation to avoid multiple instances of the same destination
                                            launchSingleTop = true

                                            // Restore state when re-selecting a previously selected item
                                            restoreState = true
                                        }


                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))

                                    //Elemento Logout centrato
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp)
                                            .clickable {
                                                adminViewModel.logout()
                                                Intent(context, MainActivity::class.java).also {
                                                    context.startActivity(it)
                                                    (context as? ComponentActivity)?.finish()
                                                }
                                            },
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ExitToApp,
                                            contentDescription = "Logout",
                                            tint = my_white
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Logout",
                                            fontSize = 25.sp,
                                            color = my_white,
                                            fontFamily = myFont,
                                        )
                                    }
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            containerColor = my_grey,
                            topBar = {
                                /*
                                TopAppBar(
                                    title = {
                                        Text(text = "PROVA")
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            }
                                        ){
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "menu"
                                            )
                                        }
                                    }
                                )

                                 */

                                TopBarDrawer(
                                    showDrawer = showDraweIcon,
                                    navController = navController,
                                    onClickDrawer = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                )
                            }
                        ) {innerPadding->
                            NavigationAdmin(
                                navController = navController,
                                modifier = Modifier.padding(innerPadding),
                                adminViewModel = adminViewModel,
                                logout = {
                                    Intent(applicationContext, MainActivity::class.java).also {
                                        startActivity(it)
                                        finish()
                                    }
                                }
                            )
                        }
                }



                }

            }
        }
    }
}