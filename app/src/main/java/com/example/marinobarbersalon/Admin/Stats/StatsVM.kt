package com.example.marinobarbersalon.Admin.Stats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti.VisualizzaAppuntamentiVM
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.google.firebase.firestore.DocumentReference
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
    // PER LA PAGINA: STATISTICHE APPUNTAMENTI

    private val _appuntamentiPerIntervallo = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val appuntamentiPerIntervallo: StateFlow<List<Pair<String, Int>>> = _appuntamentiPerIntervallo

    // PER IL CARICAMENTO
    private val _isLoading = MutableStateFlow(false)
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

//                Log.d("StatsVM", "Dati ordinati per $interval: $sortedCounts")
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
//            Log.e("StatsVM", "Errore durante il recupero delle date da Firestore", e)
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
    //SECONDA PAGINA


    private val _clientiStats = MutableStateFlow<Pair<Int, Int>>(0 to 0) //Attivi, Inattivi
    val clientiStats: StateFlow<Pair<Int, Int>> = _clientiStats

    //Stato di caricamento per  clienti
    private val _isLoadingClienti = MutableStateFlow(false)
    val isLoadingClienti: StateFlow<Boolean> = _isLoadingClienti

    private val firestore = FirebaseFirestore.getInstance()



    /**
     * Funzione per calcolare il numero di clienti attivi e inattivi
     */
    fun calcolaClientiAttiviInattivi() {
        viewModelScope.launch {
            try {
                _isLoadingClienti.value = true
                val utenti = fetchAllUsers()
                val stats = contaClientiAttiviInattivi(utenti)
                _clientiStats.value = stats
            } catch (e: Exception) {
                Log.e("StatsVM", "Errore durante il calcolo dei clienti attivi/inattivi", e)
                _clientiStats.value = 0 to 0 //Se c'è un errore, resettare i dati
            } finally {
                _isLoadingClienti.value = false
            }
        }
    }

    /**
     * Recupera tutti gli utenti dalla collezione "utenti"
     */
    private suspend fun fetchAllUsers(): List<String> {
        val utentiRef = firestore.collection("utenti")
        val utentiSnapshot = utentiRef.get().await()
        val utenti = utentiSnapshot.documents.map { it.id } //Gli ID sono gli indirizzi email degli utenti
        Log.d("StatsVM", "Numero di utenti trovati: ${utenti.size}")
        return utenti
    }

    /**
     * Conta il numero di clienti attivi e inattivi
     */
    private suspend fun contaClientiAttiviInattivi(utenti: List<String>): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -2) //Data limite = 2 mesi fa
        val twoMonthsAgoMillis = calendar.timeInMillis

        var attivi = 0
        var inattivi = 0

        for (utenteId in utenti) {
            Log.d("StatsVM", "Elaborazione utente: $utenteId")
            val appuntamenti = fetchAppuntamentiForUser(utenteId)
            Log.d("StatsVM", "Appuntamenti trovati per $utenteId: $appuntamenti")

            if (appuntamenti.isNotEmpty()) {
                val appuntamentoRecente = appuntamenti
                    .mapNotNull { appuntamento ->
                        try {
                            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            val date = formatter.parse(appuntamento.data)
                            date?.time
                        } catch (e: Exception) {
                            Log.e("StatsVM", "Errore durante il parsing della data: ${appuntamento.data}", e)
                            null
                        }
                    }
                    .maxOrNull() //Trova la data più recente

                if (appuntamentoRecente != null && appuntamentoRecente > twoMonthsAgoMillis) {
                    attivi++
                    Log.d("StatsVM", "Cliente attivo: $utenteId")
                } else {
                    inattivi++
                    Log.d("StatsVM", "Cliente inattivo: $utenteId")
                }
            } else {
                inattivi++ //Nessun appuntamento => cliente inattivo
                Log.d("StatsVM", "Cliente inattivo (nessun appuntamento): $utenteId")
            }
        }

        Log.d("StatsVM", "Totale attivi: $attivi, Totale inattivi: $inattivi")
        return attivi to inattivi
    }

    /**
     * Recupera gli appuntamenti per un utente usando la lista di DocumentReference
     */
    private suspend fun fetchAppuntamentiForUser(userId: String): List<Appuntamento> {
        val appuntamentiRef = firestore.collection("utenti").document(userId).get().await()
        val listaAppuntamenti = appuntamentiRef["appuntamenti"] as? List<DocumentReference> ?: emptyList()

        return recuperaDocumenti(listaAppuntamenti)
    }

    /**
     * Recupera i documenti della lista di referenze appuntamenti
     */
    private suspend fun recuperaDocumenti(listaAppuntamenti: List<DocumentReference>): List<Appuntamento> {
        val appuntamenti = mutableListOf<Appuntamento>()

        Log.d("StatsVM", "Numero di appuntamenti trovati: ${listaAppuntamenti.size}")
        for (document in listaAppuntamenti) {
            val result = document.get().await()
            result.toObject(Appuntamento::class.java)?.let { appuntamenti.add(it) }
        }

        Log.d("StatsVM", "Appuntamenti recuperati: $appuntamenti")
        return appuntamenti
    }
















}
























