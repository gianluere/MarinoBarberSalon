package com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.example.marinobarbersalon.Cliente.Home.User
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar

@Composable
fun CalendarScreen(
    calendarViewModel: VisualizzaAppuntamentiVM = viewModel(),
    onNavigateToNextPage: (String) -> Unit
) {
    val calendarState by calendarViewModel.calendarState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Seleziona una data",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Semplice calendario cliccabile (puoi sostituirlo con una libreria più avanzata)
        Calendar(onDateSelected = { date ->
            calendarViewModel.selectDate(date)
        })

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Data selezionata: ${calendarState.selectedDate ?: "Nessuna"}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = myFont
        )

        Button(
            onClick = {
                // Quando il pulsante è cliccato, passa la data alla funzione onNavigateToNextPage
                calendarState.selectedDate?.let { selectedDate ->
                    onNavigateToNextPage(selectedDate) // Passa la data alla pagina successiva
                }
            },
            enabled = calendarState.selectedDate != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = my_bordeaux,
                disabledContainerColor = my_bordeaux
            )
        ) {
            Text(text = "Conferma", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
        }
    }
}

@Composable
fun NextScreen(date: String) {
    val viewModel: VisualizzaAppuntamentiVM = viewModel()
    val appuntamenti by viewModel.appuntamentiState.collectAsState()

    // Esegui il recupero dei dati per la data selezionata
    LaunchedEffect(date) {
        viewModel.getAppuntamentiByDate(date) // Recupera gli appuntamenti per la data selezionata
    }

    // UI per visualizzare gli appuntamenti
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Appuntamenti per la data: $date",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Verifica se ci sono appuntamenti
        if (appuntamenti.isEmpty()) {
            Text(text = "Nessun appuntamento disponibile per questa data.", fontSize = 18.sp)
        } else {
            // Mostra gli appuntamenti
            Column(modifier = Modifier.fillMaxWidth()) {
                // Intestazioni delle colonne
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top= 8.dp)
                ) {
                    Text(
                        text = "Orario",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Nome",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Prezzo",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Servizio",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // Lista degli appuntamenti
                appuntamenti.forEach { appuntamento ->
                    AppointmentItem(appuntamento)
                }
            }
        }
    }
}






