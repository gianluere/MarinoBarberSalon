package com.example.marinobarbersalon.Admin.Stats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti.VisualizzaAppuntamentiVM
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StatsVM : ViewModel() {

    //----------------------------------------------------------------------------------------------
    //PAGINA APPUNTAMENTI


    private val visualizzaAppuntamentiVM = VisualizzaAppuntamentiVM()

    private val _appuntamentiPerIntervallo = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val appuntamentiPerIntervallo: StateFlow<List<Pair<String, Int>>> = _appuntamentiPerIntervallo

    private val _isLoading = MutableStateFlow(false) // Stato di caricamento
    val isLoading: StateFlow<Boolean> = _isLoading

    private var isFetchingData = false

//    fun getAppuntamentiPerIntervallo(interval: String) {
//        if (isFetchingData) {
//            Log.d("StatsVM", "Richiesta ignorata: una fetch è già in corso.")
//            return
//        }
//
//        viewModelScope.launch {
//            isFetchingData = true
//            Log.d("StatsVM", "Avvio fetch dati per intervallo: $interval")
//
//            try {
//                val dateCounts = fetchDateCountsFromFirestore()
//                Log.d("StatsVM", "Dati recuperati da Firestore: $dateCounts")
//
//                val appuntamentiPerData = when (interval) {
//                    "giorno" -> dateCounts.toList().sortedBy { it.first }
//                    "mese" -> dateCounts.entries
//                        .groupBy { it.key.substring(3, 10) } // Raggruppa per MM-YYYY
//                        .map { it.key to it.value.sumOf { entry -> entry.value } }
//                        .sortedBy { it.first }
//                    "anno" -> dateCounts.entries
//                        .groupBy { it.key.substring(6, 10) } // Raggruppa per YYYY
//                        .map { it.key to it.value.sumOf { entry -> entry.value } }
//                        .sortedBy { it.first }
//                    else -> emptyList()
//                }
//
//                Log.d("StatsVM", "Dati raggruppati per $interval: $appuntamentiPerData")
//
//                _appuntamentiPerIntervallo.value = appuntamentiPerData
//            } catch (e: Exception) {
//                Log.e("StatsVM", "Errore nel recupero appuntamenti", e)
//            } finally {
//                isFetchingData = false
//                Log.d("StatsVM", "Fetch dati completata.")
//            }
//        }
//    }

    fun getAppuntamentiPerIntervallo(interval: String) {
        viewModelScope.launch {
            _isLoading.value = true // Imposta il caricamento su true
            try {
                val dateCountMap = fetchDateCountsFromFirestore()
                val appuntamentiPerData = when (interval) {
                    "giorno" -> dateCountMap.toList()
                    "mese" -> countAppuntamentiPerMeseFromMap(dateCountMap)
                    "anno" -> countAppuntamentiPerAnnoFromMap(dateCountMap)
                    else -> emptyList()
                }
                _appuntamentiPerIntervallo.value = appuntamentiPerData
            } catch (e: Exception) {
                Log.e("StatsVM", "Errore nel recupero dati", e)
            } finally {
                _isLoading.value = false // Imposta il caricamento su false
            }
        }
    }




//    fun getAppuntamentiPerIntervallo(interval: String) {
//        if (isFetchingData) return
//
//        viewModelScope.launch {
//            isFetchingData = true
//            try {
//                // Recupera direttamente la mappa delle date e dei conteggi
//                val dateCountMap = fetchDateCountsFromFirestore()
//
//                val appuntamentiPerData = when (interval) {
//                    "giorno" -> dateCountMap.toList() // Usa direttamente i conteggi giornalieri
//                    "mese" -> countAppuntamentiPerMeseFromMap(dateCountMap)
//                    "anno" -> countAppuntamentiPerAnnoFromMap(dateCountMap)
//                    else -> emptyList()
//                }
//
//                Log.d("StatsVM", "Raggruppati per $interval: $appuntamentiPerData")
//                _appuntamentiPerIntervallo.value = appuntamentiPerData
//            } catch (e: Exception) {
//                Log.e("StatsVM", "Errore nel raggruppamento appuntamenti", e)
//            } finally {
//                isFetchingData = false
//            }
//        }
//    }

    suspend fun fetchDateCountsFromFirestore(): Map<String, Int> {
        val db = FirebaseFirestore.getInstance()
        val appuntamentiCollection = db.collection("appuntamenti")

        val dateCountMap = mutableMapOf<String, Int>()

        try {
            Log.d("StatsVM", "Inizio recupero appuntamenti da Firestore...")

            // Recupera tutti i documenti nella collection "appuntamenti"
            val querySnapshot = appuntamentiCollection.get().await()
            Log.d("StatsVM", "Numero di documenti principali trovati: ${querySnapshot.documents.size}")

            for (document in querySnapshot.documents) {
                val date = document.id // La data è l'ID del documento principale

                // Recupera direttamente il valore della raccolta "totale"
                val totaleSnapshot = document.reference.collection("totale").document("count").get().await()
                val totaleCount = totaleSnapshot.getLong("count")?.toInt() ?: 0

                Log.d("StatsVM", "Numero totale di appuntamenti per $date: $totaleCount")

                if (totaleCount > 0) {
                    dateCountMap[date] = totaleCount
                }
            }

            Log.d("StatsVM", "Conteggio finale per tutte le date: $dateCountMap")

        } catch (e: Exception) {
            Log.e("StatsVM", "Errore durante il recupero delle date da Firestore", e)
        }

        Log.d("StatsVM", "Fine del recupero appuntamenti.")
        return dateCountMap
    }


    // test mio
    fun getTestAppuntamenti(): List<Appuntamento> {
        return listOf(
            Appuntamento(data = "01-12-2024"),
            Appuntamento(data = "02-12-2024"),
            Appuntamento(data = "02-12-2024"),
            Appuntamento(data = "01-01-2024"),
            Appuntamento(data = "02-01-2024"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "02-01-2024"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "02-01-2024"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "02-01-2024"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "01-12-2024"),
            Appuntamento(data = "01-12-2024"),
            Appuntamento(data = "02-12-2024"),
            Appuntamento(data = "02-12-2024"),
            Appuntamento(data = "01-12-2024"),
            Appuntamento(data = "01-12-2023"),
            Appuntamento(data = "02-12-2024"),
            Appuntamento(data = "01-12-2023"),

        )
    }

    private fun countAppuntamentiPerMeseFromMap(dateCountMap: Map<String, Int>): List<Pair<String, Int>> {
        return dateCountMap.entries
            .groupBy { it.key.substring(3, 10) } // Prende solo MM-YYYY dalla data "GG-MM-YYYY"
            .map { it.key to it.value.sumOf { entry -> entry.value } } // Somma i conteggi per ogni mese
            .sortedBy { it.first } // Ordina per mese
    }

    private fun countAppuntamentiPerAnnoFromMap(dateCountMap: Map<String, Int>): List<Pair<String, Int>> {
        return dateCountMap.entries
            .groupBy { it.key.substring(6, 10) } // Prende solo YYYY dalla data "GG-MM-YYYY"
            .map { it.key to it.value.sumOf { entry -> entry.value } } // Somma i conteggi per ogni anno
            .sortedBy { it.first } // Ordina per anno
    }



    private fun countAppuntamentiPerGiorno(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val result = appuntamenti.groupBy { it.data } // Raggruppa per data completa (GG-MM-AAAA)
            .map { it.key to it.value.size } // Conta gli appuntamenti per ogni giorno
            .sortedBy { it.first } // Ordina i risultati per data (GG-MM-AAAA)
        return result
    }


    private fun countAppuntamentiPerMese(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val result = appuntamenti.groupBy {
            // Estrae mese e anno (MM-YYYY)
            val parts = it.data.split("-")
            if (parts.size == 3) "${parts[1]}-${parts[2]}" else "Formato Errato"
        }
            .filter { it.key != "Formato Errato" } // Filtra eventuali errori
            .map { it.key to it.value.size }
            .sortedBy { it.first }
        return result
    }

    private fun countAppuntamentiPerAnno(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val result = appuntamenti.groupBy {
            val parts = it.data.split("-")
            if (parts.size == 3) parts[2] else "Formato Errato"
        }
            .filter { it.key != "Formato Errato" }
            .map { it.key to it.value.size }
            .sortedBy { it.first }
        return result
    }
}







    //----------------------------------------------------------------------------------------------























