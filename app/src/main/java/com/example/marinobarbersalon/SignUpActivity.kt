package com.example.marinobarbersalon

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.ui.theme.MarinoBarberSalonTheme

class SignUpActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarinoBarberSalonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val userViewModel : UserViewModel = viewModel()
                    //val adminViewModel : AdminViewModel = viewModel()
                    //Navigation(userViewModel = userViewModel, adminViewModel= adminViewModel)
                    SignUpScreen(
                        navigaHome = {
                            Intent(applicationContext, HomeClienteActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        },
                        distruzione = { finish() },
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}