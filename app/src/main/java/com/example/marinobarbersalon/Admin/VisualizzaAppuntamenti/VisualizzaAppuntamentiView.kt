package com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar

@Composable
fun CalendarScreen(
    calendarViewModel: VisualizzaAppuntamentiVM = viewModel(),
    onNavigateToNextPage: () -> Unit
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
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { calendarViewModel.onConfirm(onNavigateToNextPage) },
            enabled = calendarState.selectedDate != null
        ) {
            Text(text = "Conferma")
        }
    }
}

@Composable
fun NextScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Pagina successiva", fontSize = 24.sp)
    }
}

