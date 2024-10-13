package com.example.marinobarbersalon

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun HomeAdmin(modifier: Modifier = Modifier, adminViewModel: AdminViewModel, onNavigateToLogin : () -> Unit) {
    val adminState by adminViewModel.adminState.collectAsState()

    LaunchedEffect(adminState.state){
        when(adminState.state){
            is AuthState.Unauthenticated -> onNavigateToLogin()
            else -> Unit
        }
    }

    Column { Text("Visualizza appuntamenti")
        Button(onClick = {
            adminViewModel.logout()
        }) {
            Text(text = "Logout")
        }



    }

}
