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

    fun getAppuntamentiPerIntervallo(interval: String) {
        if (isFetchingData) return // Evita fetch simultanei

        viewModelScope.launch {
            isFetchingData = true // Segna come in corso il recupero dati
            try {
                val allAppuntamenti = fetchAllAppuntamentiFromFirestore()

                val appuntamentiPerData = when (interval) {
                    "giorno" -> countAppuntamentiPerGiorno(allAppuntamenti)
                    "mese" -> countAppuntamentiPerMese(allAppuntamenti)
                    "anno" -> countAppuntamentiPerAnno(allAppuntamenti)
                    else -> emptyList()
                }

                _appuntamentiPerIntervallo.value = appuntamentiPerData
            } catch (e: Exception) {
                Log.e("StatsVM", "Errore nel recupero appuntamenti", e)
            } finally {
                isFetchingData = false // Rimuove il flag una volta completato il recupero
            }
        }
    }

    suspend fun fetchAllAppuntamentiFromFirestore(): List<Appuntamento> {
        val db = FirebaseFirestore.getInstance()
        val appuntamentiCollection = db.collection("appuntamenti")

        val allAppuntamenti = mutableListOf<Appuntamento>()

        // Recupero tutti i documenti nella collection "appuntamenti"
        val querySnapshot = appuntamentiCollection.get().await()

        for (document in querySnapshot.documents) {
            // Ogni documento può avere sottocollezioni con i singoli appuntamenti
            val subCollection = document.reference.collection("app").get().await()
            for (subDocument in subCollection.documents) {
                val clienteRef = subDocument.getDocumentReference("cliente")
                val servizio = subDocument.getString("servizio") ?: ""
                val descrizione = subDocument.getString("descrizione") ?: ""
                val orarioInizio = subDocument.getString("orarioInizio") ?: ""
                val orarioFine = subDocument.getString("orarioFine") ?: ""
                val prezzo = subDocument.getDouble("prezzo") ?: 0.0

                val data = document.id // La data è salvata come id del documento principale

                allAppuntamenti.add(
                    Appuntamento(
                        cliente = clienteRef,
                        servizio = servizio,
                        descrizione = descrizione,
                        orarioInizio = orarioInizio,
                        orarioFine = orarioFine,
                        prezzo = prezzo,
                        data = data
                    )
                )
            }
        }

        return allAppuntamenti
    }


    private fun countAppuntamentiPerGiorno(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val result = appuntamenti.groupBy { it.data } // Raggruppa per data completa (GG-MM-AAAA)
            .map { it.key to it.value.size } // Conta gli appuntamenti per ogni giorno
            .sortedBy { it.first } // Ordina i risultati per data (GG-MM-AAAA)
        return result
    }


    private fun countAppuntamentiPerMese(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val result = appuntamenti.groupBy {
            // Prende solo il mese e l'anno (MM-YYYY)
            it.data.substring(3, 7) + "-" + it.data.substring(6, 10) // Es: "03-2024"
        }
            .map { it.key to it.value.size } // Conta gli appuntamenti per mese
            .sortedBy { it.first } // Ordina i risultati per mese (MM-YYYY)
        return result
    }

    private fun countAppuntamentiPerAnno(appuntamenti: List<Appuntamento>): List<Pair<String, Int>> {
        val result = appuntamenti.groupBy {
            // Estrae solo l'anno (YYYY)
            it.data.substring(6, 10) // Es: "2024"
        }
            .map { it.key to it.value.size } // Conta gli appuntamenti per anno
            .sortedBy { it.first } // Ordina i risultati per anno (YYYY)
        return result

    }
}







    //----------------------------------------------------------------------------------------------























