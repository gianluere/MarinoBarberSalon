package com.example.marinobarbersalon.Admin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_grey

@Composable
fun HomeAdmin(modifier: Modifier = Modifier, adminViewModel: AdminViewModel, onNavigateToLogin : () -> Unit) {
    val adminState by adminViewModel.adminState.collectAsState()

    LaunchedEffect(adminState.state){
        when(adminState.state){
            is AuthState.Unauthenticated -> onNavigateToLogin()
            else -> Unit
        }
    }

    Column(
        modifier = modifier
    ) { Text("Visualizza appuntamenti",
              style = MaterialTheme.typography.bodyLarge
                    )
        Button(onClick = {
            adminViewModel.logout()
        }) {
            Text(text = "Logout")
        }



    }

}
