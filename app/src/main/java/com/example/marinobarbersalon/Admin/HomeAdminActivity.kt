package com.example.marinobarbersalon.Admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List

import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.marinobarbersalon.MainActivity
import com.example.marinobarbersalon.ui.theme.MarinoBarberSalonTheme
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.ui.theme.my_drawer
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import kotlinx.coroutines.launch

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
                        NavDrawerItem(
                            title = "Visualizza appuntamenti",
                            route = Screen.HomeAdmin.route,
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home,
                        ),
                        NavDrawerItem(
                            title = "Prova",
                            route = Screen.Prova.route,
                            selectedIcon = Icons.Filled.Person,
                            unselectedIcon = Icons.Outlined.Person,
                        ),
                        NavDrawerItem(
                            title = "Visualizza Appuntamenti",
                            route = Screen.VisualizzaAppuntamenti.route,
                            selectedIcon = Icons.Filled.CalendarMonth,
                            unselectedIcon = Icons.Outlined.CalendarMonth,
                        ),
                        NavDrawerItem(
                            title = "Visualizza Clienti",
                            route = Screen.VisualizzaClienti.route,
                            selectedIcon = Icons.Filled.Person,
                            unselectedIcon = Icons.Outlined.Person
                        ),
                        NavDrawerItem(
                            title = "Visualizza Servizi",
                            route = Screen.VisualizzaServizi.route,
                            selectedIcon = Icons.Filled.ContentCut,
                            unselectedIcon = Icons.Outlined.ContentCut
                        ),
                        NavDrawerItem(
                            title = "Visualizza Prodotti",
                            route = Screen.VisualizzaProdotti.route,
                            selectedIcon = Icons.Filled.ShoppingCart,
                            unselectedIcon = Icons.Outlined.ShoppingCart
                        ),
                        NavDrawerItem(
                            title = "Visualizza Recensioni",
                            route = Screen.Recensioni.route,
                            selectedIcon = Icons.Filled.Grade,
                            unselectedIcon = Icons.Outlined.Grade
                        ),
                        NavDrawerItem(
                            title = "Visualizza Statistiche",
                            route = Screen.StatsBase.route,
                            selectedIcon = Icons.Filled.Grade,
                            unselectedIcon = Icons.Outlined.Grade
                        )

                    )

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val pagineConDrawer = listOf(
                        Screen.HomeAdmin.route,
                        Screen.Prova.route
                    )

                    val showDraweIcon = currentRoute in pagineConDrawer


                    ModalNavigationDrawer(
                        gesturesEnabled = drawerState.isOpen,
                        drawerContent = {
                            ModalDrawerSheet(
                                drawerContainerColor = my_drawer,
//                                drawerContentColor = my_white,

                            ) {
                                NavDrawerHeader()

                                NavDrawerBody(items = items, currentRoute = currentRoute) { currentNavigationItem ->

                                    navController.navigate(currentNavigationItem.route){
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