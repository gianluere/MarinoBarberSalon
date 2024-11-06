package com.example.marinobarbersalon

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.marinobarbersalon.ui.theme.MarinoBarberSalonTheme

class HomeAdminActivity: ComponentActivity() {
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
                    val navController = rememberNavController()
                    //val adminState by adminViewModel.adminState.collectAsState()

                    //Parte da modificare

                    NavigationAdmin(
                        modifier = Modifier,
                        navController = navController,
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