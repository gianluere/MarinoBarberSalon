package com.example.marinobarbersalon.Admin.Servizi
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Home.Servizio
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow

/*
POSSIBILE BUG:
    quando faccio indietro bisogna ridirigere bene alla pagina indietro ( non so se non va bene ma da
    vedere meglio perche mi è successo che tornando indietro non andasse piu e rimanesse bloccato
    sulla stessa pagina)
*/




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

        if(serviziState.isNotEmpty()) {
            Text(
                text = "Servizi disponibili",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                color = my_white
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (serviziState.isEmpty()) {
                Text(
                    text = "Nessun servizio disponibile.",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = my_white
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(serviziState) { servizio ->
                        ServizioCard(
                            servizio = servizio,
                            onEliminaClick = { setServizioDaEliminare(servizio) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp)) //da chiedere se va bene o se diminuire
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
                    fontSize = 30.sp,
                    //fontFamily = myFont
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
            .padding(8.dp)
            .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
        shape = RoundedCornerShape(17.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = my_yellow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Nome: ${servizio.nome ?: "N/A"}", fontFamily = myFont, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp) , color = Color.Black)
                Text(text = "Descrizione: ${servizio.descrizione ?: "N/A"}", fontFamily = myFont, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp), color = Color.Black)
                Text(text = "Tipo: ${servizio.tipo ?: "N/A"}", fontFamily = myFont, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp), color = Color.Black)
                Text(text = "Durata: ${servizio.durata ?: 0} min", fontFamily = myFont, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp), color = Color.Black)
                Text(text = "Prezzo: €${servizio.prezzo}", style = MaterialTheme.typography.bodyMedium, fontFamily = myFont, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp), color = Color.Black)
            }

            IconButton(
                onClick = onEliminaClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = "Elimina servizio",
                    tint = my_bordeaux
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
            Text(
                text = "Eliminare il servizio?",
                fontFamily = myFont,
                fontSize = 20.sp,
            )
        },
        text = {
            Text(
                text = "Sei sicuro di voler eliminare il servizio \"${servizio.nome}\"?", // \" è l'escape
                fontFamily = myFont,
                fontSize = 20.sp,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConferma,
                modifier = Modifier
                    .padding(8.dp)
//                    .border(2.dp, my_gold, RoundedCornerShape(10.dp)),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = my_bordeaux
//                ),
                    .background(my_bordeaux)
            ) {
                Text(
                    text = "Conferma",
                    fontFamily = myFont,
                    fontSize = 20.sp,
                    color = my_white
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onAnnulla,
                modifier = Modifier
                    .padding(8.dp)
//                    .border(2.dp, my_gold, RoundedCornerShape(10.dp))
                    .background(my_bordeaux),
            ) {
                Text(
                    text = "Annulla",
                    fontFamily = myFont,
                    fontSize = 20.sp,
                    color = my_white,

                )
            }
        },
        shape = RoundedCornerShape(17.dp), // Rende il dialogo con angoli arrotondati
        modifier = Modifier
            .padding(16.dp) // Padding esterno per il dialogo
            .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
        containerColor = my_yellow, // Colore di sfondo del dialog
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
            title = {
                Text(
                text = "Errore di validazione",
                    fontFamily = myFont,
                    fontSize = 20.sp
                )
            },
            text = {
                Column {
                    formErrors.forEach { error ->
                        Text(error, color = my_bordeaux, fontFamily = myFont, fontSize = 19.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog.value = false
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, my_gold, RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = my_yellow)
                ) {
                    Text("OK", fontFamily = myFont, fontSize = 18.sp, color = my_grey)
                }
            },
            shape = RoundedCornerShape(17.dp),
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
            containerColor = my_yellow
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
            modifier = Modifier.padding(bottom = 16.dp).padding(top = 25.dp ),
            color = my_white,
            fontFamily = myFont
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = my_yellow),
            shape = RoundedCornerShape(17.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                //Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = { aggiungiServizioViewModel.onNomeChange(it) },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,         // Colore del bordo selezionato
                        focusedLabelColor = Color.Black,          // Colore della label quando il campo è selezionato
                        cursorColor = my_bordeaux,            // Colore del cursore
                        unfocusedBorderColor = Color.Black,     // Colore del bordo non selezionato
                        unfocusedLabelColor = Color.Black         // Colore della label non selezionata
                    )

                )

                Spacer(modifier = Modifier.height(8.dp))

                //Descrizione
                OutlinedTextField(
                    value = descrizione,
                    onValueChange = { aggiungiServizioViewModel.onDescrizioneChange(it) },
                    label = { Text("Descrizione") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Tipo
                Text(
                    text = "Seleziona tipo servizio:",
                    fontFamily = myFont,
                    fontSize = 25.sp,
                    color = Color.Black
//                    fontWeight = FontWeight.W500
//                    color = Color.Black.copy(alpha = 1.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    RadioButton(
                        selected = tipo == "Capelli",
                        onClick = { aggiungiServizioViewModel.onTipoChange("Capelli") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = my_bordeaux,       // Colore del cerchio e del punto interno selezionato
                            unselectedColor = my_bordeaux,        // Colore del cerchio non selezionato
                        )
                    )
                    Text(
                        text = "Capelli",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically),
                        fontFamily = myFont,
                        fontSize = 25.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = tipo == "Barba",
                        onClick = { aggiungiServizioViewModel.onTipoChange("Barba") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = my_bordeaux,
                            unselectedColor = my_bordeaux,
                        )
                    )
                    Text(
                        text ="Barba",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically),
                        fontFamily = myFont,
                        fontSize = 25.sp,
                        color = Color.Black

                    )
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
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
                },
//                modifier = Modifier
//                    .padding(8.dp),
////                    .border(2.dp, my_gold, RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)
            ) {
                Text(
                    text ="Aggiungi",
                    fontFamily = myFont,
                    fontSize = 25.sp,
                    color = my_gold
                )
            }

            Button(
                onClick = {
                    aggiungiServizioViewModel.resetFields()
                    isFormSubmitted.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)
            ) {
                Text(
                    text = "Annulla",
                    fontFamily = myFont,
                    fontSize = 25.sp,
                    color = my_white
                )
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------


