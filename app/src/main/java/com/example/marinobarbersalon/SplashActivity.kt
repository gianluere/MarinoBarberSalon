package com.example.marinobarbersalon

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.delay



class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bool = if (Build.VERSION.SDK_INT < 31) true else false
            if(bool) {
                Log.d("Splash", bool.toString())
                SplashScreen {
                    //Avvia direttamente la MainActivity senza delay
                    startActivity(Intent(this, MainActivity::class.java))

                    finish() //Chiude la SplashActivity
                }
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}


@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }
    //Layout dello Splash Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333)), //Sfondo grigio
        contentAlignment = Alignment.Center
    ) {
        //Logo al centro
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Splash Screen Logo",
            modifier = Modifier.size(200.dp) //Dim del logo
        )
    }
}
