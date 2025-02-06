package com.example.marinobarbersalon.Cliente.Home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.marinobarbersalon.Cliente.Navigation
import com.example.marinobarbersalon.Cliente.Account.NotificheClienteViewModel
import com.example.marinobarbersalon.Cliente.ScaffoldItems.BarraNavigazione
import com.example.marinobarbersalon.MainActivity
import com.example.marinobarbersalon.ui.theme.MarinoBarberSalonTheme

class HomeClienteActivity: ComponentActivity() {
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
                    val userViewModel : UserViewModel = viewModel()
                    val navController = rememberNavController()
                    val notificheClienteViewModel : NotificheClienteViewModel = viewModel()


                    //val userState by userViewModel.userState.collectAsState()
                    Scaffold(
                        bottomBar = { BarraNavigazione(navController, notificheClienteViewModel) }
                    ) {innerpadding->
                        //Navigation è il file con tutte le rotte
                        //il modifier.padding è per gli spazi nello scaffold
                        Navigation(Modifier.padding(innerpadding), navController, userViewModel, notificheClienteViewModel,
                            logout = {
                                Intent(applicationContext, MainActivity::class.java).also {
                                    startActivity(it)
                                    finish()
                                }
                            })
                    }

                }
            }
        }
    }
}