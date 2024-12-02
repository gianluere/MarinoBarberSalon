package com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Admin.Admin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


class VisualizzaAppuntamentiVM: ViewModel() {
    private val _calendarState = MutableStateFlow(CalendarState())
    val calendarState : StateFlow<CalendarState> = _calendarState.asStateFlow()

    fun selectDate(date: String) {
        _calendarState.value = CalendarState(selectedDate = date)
    }

    fun onConfirm(onNavigate: () -> Unit) {
        viewModelScope.launch {
            // Logica aggiuntiva se necessaria prima della navigazione
            onNavigate()
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(onDateSelected: (String) -> Unit) {
    // Date di esempio per un mese
    val dates = (1..31).map { day -> "2024-12-$day" }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7), // Una settimana con 7 giorni
        modifier = Modifier.padding(8.dp)
    ) {
        items(dates) { date ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onDateSelected(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = date.split("-").last())
            }
        }
    }
}
