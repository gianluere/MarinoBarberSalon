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
        if (isFetchingData) return

        viewModelScope.launch {
            isFetchingData = true
            try {
                val allAppuntamenti = getTestAppuntamenti() // Sostituire con la fetch Firebase

                val appuntamentiPerData = when (interval) {
                    "giorno" -> countAppuntamentiPerGiorno(allAppuntamenti)
                    "mese" -> countAppuntamentiPerMese(allAppuntamenti)
                    "anno" -> countAppuntamentiPerAnno(allAppuntamenti)
                    else -> emptyList()
                }

                Log.d("StatsVM", "Raggruppati per $interval: $appuntamentiPerData")
                _appuntamentiPerIntervallo.value = appuntamentiPerData
            } catch (e: Exception) {
                Log.e("StatsVM", "Errore nel raggruppamento appuntamenti", e)
            } finally {
                isFetchingData = false
            }
        }
    }

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
                Log.d("StatsVM", "Processo la data: $date")

                // Recupera la sottocollezione "app" per questa data e conta i documenti
                val subCollectionSnapshot = document.reference.collection("app").get().await()
                val subCollectionSize = subCollectionSnapshot.size()

                Log.d("StatsVM", "Numero di appuntamenti per $date: $subCollectionSize")

                if (subCollectionSize > 0) {
                    dateCountMap[date] = subCollectionSize
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























