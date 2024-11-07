package com.example.marinobarbersalon.Cliente.Account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.marinobarbersalon.Cliente.Home.UserViewModel

@Composable
fun Prenotazioni(modifier : Modifier, userViewModel: UserViewModel) {

    userViewModel.sincronizzaPrenotazioni()
    val userState by userViewModel.userState.collectAsState()
    val listaPrenotazioni by userViewModel.listaNotifiche.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(text = "Le tue prenotazioni: " + listaPrenotazioni.size)
    }

}