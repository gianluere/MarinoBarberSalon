package com.example.marinobarbersalon.Admin.VisualizzaClienti

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_yellow


@Composable
fun VisualizzaClienti(
    clientiViewModel: VisualizzaClientiVM = viewModel(),
    onNavigateToDetails: (String) -> Unit
) {
    val utentiState = clientiViewModel.usersState.collectAsState().value

    //per la ricera
    val searchText = clientiViewModel.searchText.collectAsState().value
    //val isSearching = clientiViewModel.isSearching.collectAsState().value

    LaunchedEffect(Unit) {
        if (utentiState.isEmpty()) {
            clientiViewModel.fetchUsers()
        }

    }

    //raggr. per lettera nome
    val clientiRaggruppati = utentiState
        .sortedBy { it.nome.lowercase() }
        .groupBy { it.nome.first().uppercaseChar() }

    //raggr. ricerca
    val clientiFiltrati = if (searchText.isBlank()) {
        clientiRaggruppati
    } else {
        clientiRaggruppati.mapValues { (_, clienti) ->
            clienti.filter { cliente ->
                cliente.doesMatchSearchQuery(searchText)
            }
        }.filter { it.value.isNotEmpty() }
    }

//    Log.d("HI", "eccomi sono qui")
//    Log.d("HI", "$clientiFiltrati")
//    Log.d("HI", "$clientiFiltrati")
//    Log.d("HI", "$searchText")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 64.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = clientiViewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cerca cliente...") },
            singleLine = false,
            maxLines = 1
        )


        Spacer(modifier = Modifier.height(16.dp))

        if (utentiState.isEmpty()) {
            Text(
                text = "Nessun cliente disponibile.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn {
                clientiFiltrati.forEach() { (lettera, clienti) ->
                    item {
                        Text(
                            text = lettera.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .padding(top = 8.dp),
                            textAlign = TextAlign.Start
                        )
                    }

                    items(clienti) { cliente ->
                        ClienteItem(cliente, onNavigateToDetails)
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 2.dp,
                            color = my_gold
                        )
                    }
                }
            }
        }
    }
    //Log.d("HI", "eccomi sono qui")
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
            //style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            text = cliente.cognome,
            modifier = Modifier.weight(1f),
            //style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
    }
}



@Composable
fun DettagliCliente(clienteEmail: String, clientiViewModel: VisualizzaClientiVM = viewModel()) {
    val cliente = clientiViewModel.selectedClienteState.collectAsState().value
    val context = LocalContext.current

    Log.d("DettagliCliente", "Caricamento dettagli per: $clienteEmail")

    LaunchedEffect(clienteEmail) {
        Log.d("DettagliCliente", "Richiesta dati per: $clienteEmail")
        clientiViewModel.getClienteByEmail(clienteEmail)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 128.dp)
    ) {
        if (cliente != null) {
            Log.d("DettagliCliente", "Cliente trovato: ${cliente.nome}")

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                containerColor = my_yellow
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Nome: ${cliente.nome}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontFamily = myFont,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Cognome: ${cliente.cognome}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontFamily = myFont,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Email: ${cliente.email}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontFamily = myFont,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Età: ${cliente.eta}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontFamily = myFont,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Telefono: ${cliente.telefono}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontFamily = myFont,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // Crea un Intent per aprire l'app telefono
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${cliente.telefono}")
                    }
                    context.startActivity(intent) // Avvia l'app telefono
                },

                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                )

            ) {
                Text(text = "Chiama ${cliente.nome}", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
            }

        } else {
            Log.d("DettagliCliente", "Cliente non trovato.")
            Text(text = "Cliente non trovato.")
        }
    }
}
