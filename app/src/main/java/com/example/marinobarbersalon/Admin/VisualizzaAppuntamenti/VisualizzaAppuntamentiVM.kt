package com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Admin.Admin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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