package com.example.marinobarbersalon.Admin.VisualizzaClienti

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Home.UserFirebase
import com.example.marinobarbersalon.ui.theme.myFont
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun VisualizzaClienti(
    clientiViewModel: VisualizzaClientiVM = viewModel(),
    onNavigateToDetails: (String) -> Unit
) {
    val utentiState = clientiViewModel.usersState.collectAsState().value

    LaunchedEffect(Unit) {
        if (utentiState.isEmpty()) {
            clientiViewModel.fetchUsers()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Elenco Clienti",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = myFont
        )

        if (utentiState.isEmpty()) {
            Text(
                text = "Nessun cliente disponibile.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn {
                items(utentiState) { cliente ->
                    ClienteItem(cliente, onNavigateToDetails)
                    Log.d("Hi", "${cliente.email}")

                }
            }
        }
    }
    Log.d("HI", "eccomi sono qui")

}

@Composable
fun ClienteItem(cliente: UserFirebase, onNavigateToDetails: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNavigateToDetails(cliente.email) }
    ) {
        Text(
            text = cliente.nome,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            text = cliente.cognome,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun DettagliCliente(clienteEmail: String, clientiViewModel: VisualizzaClientiVM = viewModel()) {
    val cliente = clientiViewModel.selectedClienteState.collectAsState().value

    Log.d("DettagliCliente", "Caricamento dettagli per: $clienteEmail")

    LaunchedEffect(clienteEmail) {
        Log.d("DettagliCliente", "Richiesta dati per: $clienteEmail")
        clientiViewModel.getClienteByEmail(clienteEmail)
    }

    if (cliente != null) {
        Log.d("DettagliCliente", "Cliente trovato: ${cliente.nome}")

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nome: ${cliente.nome}")
            Text(text = "Cognome: ${cliente.cognome}")
            Text(text = "Email: ${cliente.email}")
            Text(text = "Età: ${cliente.eta}")
            Text(text = "Telefono: ${cliente.telefono}")
        }

        Log.d("HI", "Cliente Nome: ${cliente.nome}")
        Log.d("HI", "Cliente Cognome: ${cliente.cognome}")
        Log.d("HI", "Cliente Email: ${cliente.email}")
        Log.d("HI", "Cliente Età: ${cliente.eta}")
    } else {
        Log.d("DettagliCliente", "Cliente non trovato.")
        Text(text = "Cliente non trovato.")
    }
}
