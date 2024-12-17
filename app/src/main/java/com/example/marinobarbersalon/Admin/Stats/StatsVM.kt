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
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class StatsVM : ViewModel() {

    //----------------------------------------------------------------------------------------------
    // PER LA PAGINA APPUNTAMENTI

    private val _appuntamentiPerIntervallo = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val appuntamentiPerIntervallo: StateFlow<List<Pair<String, Int>>> = _appuntamentiPerIntervallo

    // PER IL CARICAMENTO
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private var isFetchingData = false
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // FUNZIONI STATISTICHE APPUNTAMENTI

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

//    fun getAppuntamentiPerIntervallo(interval: String) {
//        viewModelScope.launch {
//            _isLoading.value = true // Imposta il caricamento su true
//            try {
//                val dateCountMap = fetchDateCountsFromFirestore()
//                val appuntamentiPerData = when (interval) {
//                    "giorno" -> dateCountMap.toList()
//                    "mese" -> countAppuntamentiPerMeseFromMap(dateCountMap)
//                    "anno" -> countAppuntamentiPerAnnoFromMap(dateCountMap)
//                    else -> emptyList()
//                }
//                _appuntamentiPerIntervallo.value = appuntamentiPerData
//            } catch (e: Exception) {
//                Log.e("StatsVM", "Errore nel recupero dati", e)
//            } finally {
//                _isLoading.value = false // Imposta il caricamento su false
//            }
//        }
//    }

    fun getAppuntamentiPerIntervallo(interval: String) {
        viewModelScope.launch {
            if (isFetchingData) return@launch // Previene più fetch simultanei
            _isLoading.value = true // Imposta il caricamento su true
            isFetchingData = true

            try {
                // Recupera i dati da Firestore (mappa con date -> conteggio)
                val rawCounts = fetchDateCountsFromFirestore()

                // Elabora e ordina i dati in base all'intervallo selezionato
                val sortedCounts = when (interval) {
                    "giorno" -> rawCounts
                        .map { it.key to it.value }
                        .sortedBy { LocalDate.parse(it.first, DateTimeFormatter.ofPattern("dd-MM-yyyy")) }
                    "mese" -> rawCounts
                        .map { it.key.substring(3, 10) to it.value } // Estrae "MM-yyyy"
                        .groupBy { it.first } // Raggruppa per mese
                        .map { it.key to it.value.sumOf { pair -> pair.second } } // Somma i conteggi per mese
                        .sortedBy { YearMonth.parse(it.first, DateTimeFormatter.ofPattern("MM-yyyy")) }
                    "anno" -> rawCounts
                        .map { it.key.substring(6, 10) to it.value } // Estrae "yyyy"
                        .groupBy { it.first } // Raggruppa per anno
                        .map { it.key to it.value.sumOf { pair -> pair.second } } // Somma i conteggi per anno
                        .sortedBy { it.first.toInt() } // Ordina per anno
                    else -> emptyList()
                }

                // Aggiorna il valore dello StateFlow
                _appuntamentiPerIntervallo.value = sortedCounts

                Log.d("StatsVM", "Dati ordinati per $interval: $sortedCounts")
            } catch (e: Exception) {
                Log.e("StatsVM", "Errore nel raggruppamento appuntamenti", e)
            } finally {
                _isLoading.value = false // Fine caricamento
                isFetchingData = false
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
//            Log.d("StatsVM", "Inizio recupero appuntamenti da Firestore...")

            //Recupera tutti i documenti nella collection "appuntamenti"
            val querySnapshot = appuntamentiCollection.get().await()
//            Log.d("StatsVM", "Numero di documenti principali trovati: ${querySnapshot.documents.size}")

            for (document in querySnapshot.documents) {
                val date = document.id //La data è l'ID del documento principale

                // Recupera direttamente il valore della raccolta "totale"
                val totaleSnapshot = document.reference.collection("totale")
                                    .document("count").get().await()

                val totaleCount = totaleSnapshot.getLong("count")?.toInt() ?: 0

//                Log.d("StatsVM", "Numero totale di appuntamenti per $date: $totaleCount")

                if (totaleCount > 0) {
                    dateCountMap[date] = totaleCount
                }
            }

//            Log.d("StatsVM", "Conteggio finale per tutte le date: $dateCountMap")

        } catch (e: Exception) {
            Log.e("StatsVM", "Errore durante il recupero delle date da Firestore", e)
        }

//        Log.d("StatsVM", "Fine del recupero appuntamenti.")
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
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // FUNZIONI SECONDA PAGINA












}
























