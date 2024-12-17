package com.example.marinobarbersalon.Admin.Stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti.VisualizzaAppuntamentiVM
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class StatsVM : ViewModel() {

    //----------------------------------------------------------------------------------------------
    //PAGINA APPUNTAMENTI
    private val visualizzaAppuntamentiVM = VisualizzaAppuntamentiVM()

    private val _appuntamentiPerIntervallo = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val appuntamentiPerIntervallo: StateFlow<List<Pair<String, Int>>> = _appuntamentiPerIntervallo

    // Funzione per ottenere gli appuntamenti per intervallo
    fun getAppuntamentiPerIntervallo(interval: String) {
        viewModelScope.launch {
            try {
                val appuntamenti = visualizzaAppuntamentiVM.fetchAppuntamentiFromFirestore("04-12-2024") // Passa la data o altro filtro
                val appuntamentiPerData = when (interval) {
                    "giorno" -> countAppuntamentiPerGiorno(appuntamenti)
                    "settimana" -> countAppuntamentiPerSettimana(appuntamenti)
                    "mese" -> countAppuntamentiPerMese(appuntamenti)
                    else -> emptyList()
                }
                _appuntamentiPerIntervallo.value = appuntamentiPerData
            } catch (e: Exception) {
                Log.e("StatsVM", "Errore nel recupero appuntamenti", e)
            }
        }
    }

    //Calcola gli appuntamenti per giorno
    private fun countAppuntamentiPerGiorno(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return appuntamenti.groupBy { dateFormat.format(SimpleDateFormat("yyyy-MM-dd").parse(it.data)) }
            .map { it.key to it.value.size }
            .sortedBy { it.first }
    }

    //Calcola gli appuntamenti per settimana
    private fun countAppuntamentiPerSettimana(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val weekFormat = SimpleDateFormat("w-yyyy", Locale.getDefault()) // Format per settimana
        return appuntamenti.groupBy { weekFormat.format(SimpleDateFormat("yyyy-MM-dd").parse(it.data)) }
            .map { it.key to it.value.size }
            .sortedBy { it.first }
    }

    //Calcola gli appuntamenti per mese
    private fun countAppuntamentiPerMese(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val monthFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault()) // Format per mese
        return appuntamenti.groupBy { monthFormat.format(SimpleDateFormat("yyyy-MM-dd").parse(it.data)) }
            .map { it.key to it.value.size }
            .sortedBy { it.first }
    }


    //----------------------------------------------------------------------------------------------




}

