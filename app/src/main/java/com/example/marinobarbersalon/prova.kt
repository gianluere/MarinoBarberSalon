package com.example.marinobarbersalon

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Prova(modifier: Modifier = Modifier, adminViewModel: AdminViewModel) {
    Column { Text("PROVAPROVA")
        Button(onClick = {
            adminViewModel.logout()
        }) {
            Text(text = "Logout")
        }

    }

}
