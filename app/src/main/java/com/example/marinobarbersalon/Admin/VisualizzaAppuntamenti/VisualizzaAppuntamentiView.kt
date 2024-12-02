package com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
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
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = myFont
        )

        Button(
            onClick = { calendarViewModel.onConfirm(onNavigateToNextPage) },
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
fun NextScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Pagina successiva", fontSize = 24.sp)
    }
}

