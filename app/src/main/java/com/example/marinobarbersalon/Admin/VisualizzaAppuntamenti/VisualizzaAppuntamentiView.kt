package com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun VisualizzaAppuntamenti(
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
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = myFont,
            color = my_white
        )
        Calendar(onDateSelected = { date ->
            calendarViewModel.selectDate(date)
        })

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Data selezionata: ${calendarState.selectedDate ?: "Nessuna"}",
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = myFont,
            color = my_white,
            fontSize = 30.sp
        )

        Button(
            onClick = {
                calendarState.selectedDate?.let { selectedDate ->
                    onNavigateToNextPage(selectedDate)
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
fun VisualizzaAppuntamenti1(date: String, viewModel: VisualizzaAppuntamentiVM = viewModel()) {
    val appuntamenti by viewModel.appuntamentiState.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState().value

    LaunchedEffect(date) {
        viewModel.getAppuntamentiByDate(date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Appuntamenti per la data: $date",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(top = 85.dp),
            fontFamily = myFont,
            color = my_white
        )

        if (isLoading) {
            // Mostra il CircularProgressIndicator durante il caricamento
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = my_gold,
                    modifier = Modifier.size(100.dp)
                )
            }
        } else {
            if (appuntamenti.isEmpty()) {
                // Mostra il messaggio quando non ci sono appuntamenti
                Text(
                    text = "Nessun appuntamento disponibile per questa data.",
                    fontSize = 18.sp,
                    fontFamily = myFont,
                    color = my_white
                )
            } else {
                // Mostra la lista degli appuntamenti
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(appuntamenti) { appuntamento ->
                        AppointmentItem(appuntamento)
                    }
                }
            }
        }
    }
}







