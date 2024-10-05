package com.example.marinobarbersalon

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/*
class ListaGiorniViewModelFactory(application: Application, servizio : Servizio) :
    ViewModelProvider.Factory {
    private val mApplication: Application
    private val mservizio: Servizio
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListaGiorniViewModel(mApplication, mservizio) as T
    }
    init {
        mApplication = application
        mservizio = servizio
    }
}

 */


class ListaGiorniViewModelFactory(private val servizio: Servizio) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaGiorniViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListaGiorniViewModel(servizio) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



class ListaGiorniViewModel(private val servizio : Servizio) : ViewModel() {

    private val _listaGiorni = MutableStateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>>(emptyList())
    val listaGiorni: StateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>> = _listaGiorni.asStateFlow()

    private val _listaGiorniOccupati = MutableStateFlow<List<Pair<LocalDate, List<LocalTime>>>>(emptyList())
    val listaGiorniOccupati : StateFlow<List<Pair<LocalDate, List<LocalTime>>>> = _listaGiorniOccupati.asStateFlow()

    private val _listaGiorniAggiornata = MutableStateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>>(emptyList())
    val listaGiorniAggiornata: StateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>> = _listaGiorniAggiornata.asStateFlow()

    private val giorniFestivi = listOf(
        LocalDate.of(2024, 1, 1),  // Capodanno
        LocalDate.of(2024, 12, 25), // Natale
        LocalDate.of(2024, 12, 26), // Santo Stefano
        LocalDate.of(2024, 4, 25),  // Festa della Liberazione
        LocalDate.of(2024, 8, 15)   // Ferragosto
    )



    init {
            viewModelScope.launch {
                try{
                    // Genera la lista dei giorni
                    _listaGiorni.value = generateListaDate(LocalDate.now(), 60, giorniFestivi)
                    _listaGiorniAggiornata.value = generateListaDate(LocalDate.now(), 60, giorniFestivi)

                    val prova = async{ generaListaOccupatiSospend(LocalDate.now(), 60)}
                    _listaGiorniOccupati.value = prova.await()
                    // Genera la lista occupati
                    //_listaGiorniOccupati.value = generaListaOccupatiSospend(LocalDate.now(), 60)
                    // Aggiorna la lista dei giorni con quelli occupati
                    val lista = aggiornaListaOccupati(_listaGiorni.value, _listaGiorniOccupati.value)
                    _listaGiorniAggiornata.value = lista

                    Log.d("ViewModelInit", "Lista aggiornata: ${_listaGiorniAggiornata.value}")


                    //_listaGiorniAggiornata.value =_listaGiorni.value
                }catch (e: Exception){
                    Log.e("ViewModelInit", "Errore durante l'inizializzazione: ${e.message}", e)
                }
            }



    }


    fun generateListaDate(
        oggi: LocalDate,
        giorniTotali: Int,
        giorniFestivi: List<LocalDate>
    ): List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>> {
        val giorniDisponibili = mutableListOf<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>()

        val orarioInizio = LocalTime.of(9, 0)
        val orarioFine = LocalTime.of(20, 0)
        val durataSlot = 30L

        for (i in 0..giorniTotali) {
            val giornoCorrente = oggi.plusDays(i.toLong())

            // Escludi sabato, domenica e giorni festivi
            if (giornoCorrente.dayOfWeek == DayOfWeek.SATURDAY ||
                giornoCorrente.dayOfWeek == DayOfWeek.SUNDAY ||
                giorniFestivi.contains(giornoCorrente)) {
                // Aggiungi il giorno senza orari disponibili
                giorniDisponibili.add(giornoCorrente to emptyList())
            } else {
                val slotOrari = mutableListOf<Pair<LocalTime, LocalTime>>()

                var orarioCorrente = orarioInizio
                while (orarioCorrente.isBefore(orarioFine)) {
                    val orarioSuccessivo = orarioCorrente.plusMinutes(durataSlot)
                    slotOrari.add(orarioCorrente to orarioSuccessivo)
                    orarioCorrente = orarioSuccessivo
                }

                giorniDisponibili.add(giornoCorrente to slotOrari)
            }
        }

        return giorniDisponibili
    }



    private suspend fun generaListaOccupatiSospend(
        oggi: LocalDate,
        giorniTotali: Int
    ): List<Pair<LocalDate, List<LocalTime>>> {
        val db = FirebaseFirestore.getInstance()
        val listaOccupati = mutableListOf<Pair<LocalDate, List<LocalTime>>>()
        val ultimo = oggi.plusDays(giorniTotali.toLong())

        try {
            // Chiamata Firestore bloccante con await()
            val giorni = db.collection("occupati").get().await()

            for (giorno in giorni) {
                val dati = giorno.data
                val giornoCorrente = LocalDate.parse(giorno.id, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

                Log.d("ListaGiorniViewModel", "Elaborando il giorno: $giornoCorrente")
                Log.d("ListaGiorniViewModel", giorno.id)

                if (giornoCorrente.isEqual(oggi) ||
                    (giornoCorrente.isAfter(oggi) && giornoCorrente.isBefore(ultimo)) ||
                    giornoCorrente.isEqual(ultimo)
                ) {
                    val slotOrari = mutableListOf<LocalTime>()
                    for (key in dati.keys) {
                        Log.d("ListaGiorniViewModel", key)
                        slotOrari.add(
                            LocalTime.parse(
                                key.toString(),
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                        )
                    }
                    slotOrari.sort()
                    listaOccupati.add(giornoCorrente to slotOrari)
                }
            }

            Log.d("ListaGiorniViewModel", "Chiamata a Firestore per ottenere i giorni occupati")

        } catch (e: Exception) {
            Log.e("ListaGiorniViewModel", "Errore durante l'accesso a Firestore: ${e.message}", e)
        }

        Log.d("Stampa", listaOccupati.toString())

        return listaOccupati
    }


    private fun aggiornaListaOccupati(
        listaOrari: List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>,
        listaOccupati: List<Pair<LocalDate, List<LocalTime>>>
    ): List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>> {

        // Crea una nuova lista modificata a partire dalla listaOrari
        return listaOrari.map { (data, orariDisponibili) ->
            Log.d("ListaGiorniViewModel", data.toString())
            // Trova se esiste una corrispondenza nella listaOccupati per la data corrente
            val occupatiPerQuestaData = listaOccupati.find { it.first == data }?.second


            // Se ci sono orari occupati per questa data, rimuovili da orariDisponibili
            val orariAggiornati = if (occupatiPerQuestaData != null) {
                Log.d("ListaGiorniViewModel", "ciao")
                /*orariDisponibili.filterNot {
                    val isOcc = occupatiPerQuestaData.contains(it.first)
                    Log.d("ListaGiorniViewModel", "Orario: ${it.first}, Occupato: $isOcc")
                    isOcc
                    //val isOccupato = occupatiPerQuestaData.contains(orarioInizio)
                    //Log.d("ListaGiorniViewModel", "Orario: $orarioInizio, Occupato: $isOccupato")
                    //isOccupato
                }
                 */
                val slotOrari = mutableListOf<Pair<LocalTime, LocalTime>>()
                var orarioCorrente = LocalTime.of(9,0)
                while (orarioCorrente.isBefore(LocalTime.of(20,0))) {
                    val orarioSuccessivo = orarioCorrente.plusMinutes(30)
                    //Verifica se l'orario corrente è occupato confrontando solo ore e minuti
                    val isOccupato = occupatiPerQuestaData.any { occupato ->
                        occupato.hour == orarioCorrente.hour && occupato.minute == orarioCorrente.minute
                                && occupatiPerQuestaData.contains(orarioSuccessivo)
                    }

                    if (!isOccupato) {
                        slotOrari.add(orarioCorrente to orarioSuccessivo)
                    }
                    orarioCorrente = orarioSuccessivo
                }
                //Log.d("Stampa", slotOrari.toString())
                slotOrari
            } else {
                Log.d("ListaGiorniViewModel", "Diverso")
                orariDisponibili
            }
            Log.d("Stampa", data.toString())
            Log.d("Stampa", orariAggiornati.toString())



            // Restituisci la data con la nuova lista di orari aggiornati
            data to orariAggiornati
        }
    }

}