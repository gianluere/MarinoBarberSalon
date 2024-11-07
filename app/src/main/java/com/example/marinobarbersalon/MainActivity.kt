package com.example.marinobarbersalon

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Admin.AdminViewModel
import com.example.marinobarbersalon.Admin.HomeAdminActivity
import com.example.marinobarbersalon.Cliente.Home.HomeClienteActivity
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.ui.theme.MarinoBarberSalonTheme

class MainActivity : ComponentActivity() {
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
                    val adminViewModel : AdminViewModel = viewModel()
                    //Navigation(userViewModel = userViewModel, adminViewModel= adminViewModel)
                    LoginScreen(
                        navigaHomeCliente = {
                            Intent(applicationContext, HomeClienteActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        },
                        navigaHomeAdmin = {
                            Intent(applicationContext, HomeAdminActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        },
                        navigaSignUp = {
                            Intent(applicationContext, SignUpActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        userViewModel = userViewModel,
                        adminViewModel = adminViewModel
                    )
                }
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MarinoBarberSalonTheme {
        Greeting("Android")
    }
}*/