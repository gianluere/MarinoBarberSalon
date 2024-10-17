package com.example.marinobarbersalon.Admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marinobarbersalon.AuthState

@Composable
fun PaginaProva(modifier: Modifier = Modifier, adminViewModel: AdminViewModel, onNavigateToLogin : () -> Unit) {


    Column {

        Spacer(modifier = Modifier.padding(100.dp))
        Button(onClick = {
            adminViewModel.logout()
        }) {
            Text(text = "Logout")
        }

        Row{
            Text(text="SEI ARRIVATO NELLA PAGINA DI PROVA")

        }
    }
}