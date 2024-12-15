package com.example.marinobarbersalon.Admin.Servizi
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Home.Servizio
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white


//--------------------------------------------------------------------------------------------------
//PRIMA PAGINA
@Composable
fun VisualizzaServizi(
    serviziViewModel: VisualizzaServiziVM = viewModel(),
    onNavigateToAddServizio: () -> Unit
) {
    val serviziState = serviziViewModel.serviziState.collectAsState().value
    val (servizioDaEliminare, setServizioDaEliminare) = remember { mutableStateOf<Servizio?>(null) }

    LaunchedEffect(Unit) {
        serviziViewModel.fetchServizi()
//        if (serviziState.isEmpty()) {
//            serviziViewModel.fetchServizi()
//        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Servizi disponibili",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            color = my_white
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (serviziState.isEmpty()) {
                Text(
                    text = "Nessun servizio disponibile.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(serviziState) { servizio ->
                        ServizioCard(
                            servizio = servizio,
                            onEliminaClick = { setServizioDaEliminare(servizio) }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = onNavigateToAddServizio,
                containerColor = my_bordeaux,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(
                    "+",
                    color = my_gold,
                )
            }
        }

        //Dialog eliminaz servizio
        servizioDaEliminare?.let { servizio ->
            ConfermaEliminazioneDialog(
                servizio = servizio,
                onConferma = {
                    serviziViewModel.deleteServizio(servizio)
                    setServizioDaEliminare(null)
                },
                onAnnulla = {
                    setServizioDaEliminare(null)
                }
            )
        }
    }
}

@Composable
fun ServizioCard(servizio: Servizio, onEliminaClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Nome: ${servizio.nome ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Descrizione: ${servizio.descrizione ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Tipo: ${servizio.tipo ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Durata: ${servizio.durata ?: 0} min", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Prezzo: €${servizio.prezzo}", style = MaterialTheme.typography.bodyMedium)
            }

            IconButton(
                onClick = onEliminaClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = "Elimina servizio",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun ConfermaEliminazioneDialog(
    servizio: Servizio,
    onConferma: () -> Unit,
    onAnnulla: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onAnnulla,
        title = {
            Text(text = "Eliminare il servizio?")
        },
        text = {
            Text(text = "Sei sicuro di voler eliminare il servizio \"${servizio.nome}\"?") // \" è l'escape
        },
        confirmButton = {
            TextButton(onClick = onConferma) {
                Text(text = "Conferma")
            }
        },
        dismissButton = {
            TextButton(onClick = onAnnulla) {
                Text(text = "Annulla")
            }
        }
    )
}
//--------------------------------------------------------------------------------------------------



//--------------------------------------------------------------------------------------------------
//SECONDA PAGINA
@Composable
fun AggiungiServizio(
    aggiungiServizioViewModel: VisualizzaServiziVM = viewModel(),
    onAggiungiSuccess: () -> Unit,
    onAnnullaClick: () -> Unit
) {
    val nome = aggiungiServizioViewModel.nome.collectAsState().value
    val descrizione = aggiungiServizioViewModel.descrizione.collectAsState().value
    val tipo = aggiungiServizioViewModel.tipo.collectAsState().value
    val durata = aggiungiServizioViewModel.durata.collectAsState().value
    val prezzo = aggiungiServizioViewModel.prezzo.collectAsState().value

    //form val
    val formErrors = aggiungiServizioViewModel.formErrors.collectAsState().value
    val showErrorDialog = remember { mutableStateOf(false) }
    val isFormSubmitted = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        aggiungiServizioViewModel.validateForm()
        if (formErrors.isNotEmpty()) {
            showErrorDialog.value = true
        }
    }


    if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = { Text("Errore di validazione") },
            text = {
                Column {
                    formErrors.forEach { error ->
                        Text(error, color = Color.Red)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aggiungi Servizio",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = my_white
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                //Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = { aggiungiServizioViewModel.onNomeChange(it) },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Descrizione
                OutlinedTextField(
                    value = descrizione,
                    onValueChange = { aggiungiServizioViewModel.onDescrizioneChange(it) },
                    label = { Text("Descrizione") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Tipo
                Text(text = "Seleziona tipo servizio:", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    RadioButton(
                        selected = tipo == "Capelli",
                        onClick = { aggiungiServizioViewModel.onTipoChange("Capelli") }
                    )
                    Text("Capelli", modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically))

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = tipo == "Barba",
                        onClick = { aggiungiServizioViewModel.onTipoChange("Barba") }
                    )
                    Text("Barba", modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically))
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Durata
                OutlinedTextField(
                    value = if (durata == 0) "" else durata.toString(),
                    onValueChange = { newValue ->
                        val validInput = newValue.toIntOrNull() != null || newValue.isEmpty()

                        if (validInput) {
                            aggiungiServizioViewModel.onDurataChange(newValue.toIntOrNull() ?: 0)
                        }
                    },
                    label = { Text("Durata (minuti)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Prezzo
                OutlinedTextField(
                    value = if (prezzo == 0.0) "" else prezzo.toString(),
                    onValueChange = { newValue ->
                        val cleanedValue = newValue.replace(',', '.')
                        val validValue = cleanedValue.toDoubleOrNull()
                        if (validValue != null) {
                            aggiungiServizioViewModel.onPrezzoChange(validValue)
                        }
                    },
                    label = { Text("Prezzo (€)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    isFormSubmitted.value = true
                    aggiungiServizioViewModel.validateForm()
                    if (formErrors.isEmpty()) {
                        aggiungiServizioViewModel.aggiungiServizio(
                            onSuccess = onAggiungiSuccess,
                            onError = { /* Mostra errore */ }
                        )
                    } else {
                        showErrorDialog.value = true
                    }
                }
            ) {
                Text("Aggiungi")
            }

            Button(
                onClick = {
                    aggiungiServizioViewModel.resetFields()
                    isFormSubmitted.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Annulla")
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------


