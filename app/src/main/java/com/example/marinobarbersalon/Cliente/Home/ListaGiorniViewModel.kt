package com.example.marinobarbersalon.Cliente.Home

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


//Factory per istanziare l'oggetto servizio
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


    private val db = FirebaseFirestore.getInstance()
    private val _listaGiorni = MutableStateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>>(emptyList())

    private val _listaGiorniOccupati = MutableStateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>>(emptyList())

    private val _listaGiorniAggiornata = MutableStateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>>(emptyList())
    val listaGiorniAggiornata: StateFlow<List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>> = _listaGiorniAggiornata.asStateFlow()

    private val giorniFestivi = listOf(
        LocalDate.of(2024, 1, 1),  //Capodanno
        LocalDate.of(2024, 1, 6),  //Epifania
        LocalDate.of(2024, 12, 25), //Natale
        LocalDate.of(2024, 12, 26), //Santo Stefano
        LocalDate.of(2024, 4, 25),  //Festa della Liberazione
        LocalDate.of(2024, 8, 15)   //Ferragosto
    )



    init {
        viewModelScope.launch {
            try {
                // Genera la lista dei giorni
                _listaGiorni.value = generateListaDate(LocalDate.now(), 60, giorniFestivi)
                _listaGiorniAggiornata.value = generateListaDate(LocalDate.now(), 60, giorniFestivi)

                //Richiede un ulteriore punto di sincronizzazione
                val prova = async{ generaListaOccupatiSuspend(LocalDate.now(), 60)}
                _listaGiorniOccupati.value = prova.await()

                //Aggiorna la lista dei giorni con quelli occupati
                val lista = aggiornaListaOccupati(_listaGiorni.value, _listaGiorniOccupati.value)
                _listaGiorniAggiornata.value = lista

                //Se il servizio dura più di 30 minuti, aggiorna gli slot
                if (servizio.durata!! > 30) {
                    _listaGiorniAggiornata.value = aggiornaSlotdaSessanta(_listaGiorniAggiornata.value)
                }

            } catch (e: Exception) {
                Log.e("ViewModelInit", "Errore durante l'inizializzazione: ${e.message}", e)
            }
        }

    }


    //Ritorna il primo orario disponibile su cui iniziare a calcolare gli slot
    /**
     * Esempio: ora attuale 10:20 => ritorna 10:30
     * Esempio: ora attuale 10:31 => ritorna 11:00
     * Esemio: ora attuale 10:00 oppure 10:30 => ritorna 10:00 oppure 10:30
     * */
    private fun primoOrarioDisponibile(currentTime: LocalTime): LocalTime {
        val nextMinute = if (currentTime.minute % 30 == 0 || currentTime.minute == 0) {
            currentTime.minute
        } else {
            (currentTime.minute / 30 + 1) * 30
        }

        Log.d("OrarioIniziale", "CurrTime: " + currentTime.minute.toString())
        Log.d("OrarioIniziale", nextMinute.toString())

        return currentTime.withMinute(nextMinute % 60).withHour(currentTime.hour + nextMinute / 60)
            .withSecond(0).withNano(0)
    }

    fun generateListaDate(
        oggi: LocalDate,
        giorniTotali: Int,
        giorniFestivi: List<LocalDate>
    ): List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>> {

        //E' una lista fatta da elementi pair, dove a ogni data è associata la lista degli slot orari
        val giorniDisponibili = mutableListOf<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>()

        val orarioInizio = LocalTime.of(9, 0)
        val orarioFine = LocalTime.of(20, 0)
        val durataSlot = 30L

        //Per ogni giorno genero gli slot
        for (i in 0..giorniTotali) {
            val giornoCorrente = oggi.plusDays(i.toLong())

            //Escludi domenica, lunedì e giorni festivi
            if (giornoCorrente.dayOfWeek == DayOfWeek.SUNDAY ||
                giornoCorrente.dayOfWeek == DayOfWeek.MONDAY ||
                giorniFestivi.contains(giornoCorrente)) {
                //Aggiungo il giorno senza orari disponibili
                giorniDisponibili.add(giornoCorrente to emptyList())
            } else {
                val slotOrari = mutableListOf<Pair<LocalTime, LocalTime>>()

                //controllo se l'orario attuale di oggi è maggiore delle 09:00
                var orarioCorrente = if (i == 0 && LocalTime.now().isAfter(orarioInizio)) {
                    //Calcolo il primo orario disponibile dato che le 9:00 di oggi sono già passate
                    primoOrarioDisponibile(LocalTime.now())
                } else {
                    orarioInizio
                }

                while (orarioCorrente.isBefore(orarioFine)) {
                    //Aggiungo slot da 30 minuti fino ad arrivare alle 20:00
                    val orarioSuccessivo = orarioCorrente.plusMinutes(durataSlot)
                    slotOrari.add(orarioCorrente to orarioSuccessivo)
                    orarioCorrente = orarioSuccessivo
                }

                //Aggiungo il pair alla lista
                giorniDisponibili.add(giornoCorrente to slotOrari)
            }
        }

        return giorniDisponibili
    }



    private suspend fun generaListaOccupatiSuspend(
        oggi: LocalDate,
        giorniTotali: Int
    ): List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>> {

        //E' una lista fatta da elementi pair, dove a ogni data è associata la lista degli slot orari
        val listaOccupati = mutableListOf<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>()
        val ultimo = oggi.plusDays(giorniTotali.toLong())

        try {
            //Chiamata Firestore bloccante con await()
            val giorni = db.collection("occupati").get().await()

            for (giorno in giorni) {
                val dati = giorno.data
                //i miei documenti su firestore sono posti con format dd-MM-yyyy
                val giornoCorrente = LocalDate.parse(giorno.id, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

                Log.d("ListaGiorniViewModel", "Elaborando il giorno: $giornoCorrente")

                //qui ricerco solo i giorni che sono occupati e che si trovano da oggi a oggi + 3mesi, gli altri non mi interessano
                if (giornoCorrente.isEqual(oggi) ||
                    (giornoCorrente.isAfter(oggi) && giornoCorrente.isBefore(ultimo)) ||
                    giornoCorrente.isEqual(ultimo)
                ) {

                    val slotOrari = mutableListOf<Pair<LocalTime, LocalTime>>()
                    //dati.keys.sorted()

                    //le chiavi sono definite come "hh:mm-hh:mm" e ne faccio l'ordinamento
                    val orariOrdinati = dati.keys.sortedBy { intervallo ->
                        val orarioInizio = intervallo.split("-")[0].trim()
                        LocalTime.parse(orarioInizio, DateTimeFormatter.ofPattern("HH:mm"))
                    }

                    for (key in orariOrdinati) {

                        //le chiavi sono definite come "hh:mm-hh:mm" prendo orarioInizio e orarioFine
                        val oraInizio =  LocalTime.parse(key.take(5), DateTimeFormatter.ofPattern("HH:mm"))
                        Log.d("Sorted", "OrIn: $oraInizio")
                        val oraFine = LocalTime.parse(key.substring(6, 11), DateTimeFormatter.ofPattern("HH:mm"))
                        Log.d("Sorted", "OrIn: $oraFine")

                        val durataSlot = oraFine.toSecondOfDay() - oraInizio.toSecondOfDay()

                        // Se la durata è di almeno un'ora aggiungo due slot da 30 minuti
                        if (durataSlot >= 60 * 60) {
                            val primoSlotFine = oraInizio.plusMinutes(30)
                            slotOrari.add(oraInizio to primoSlotFine)
                            slotOrari.add(primoSlotFine to oraFine)

                        } else {
                            //Aggiungo lo slot originale
                            slotOrari.add(oraInizio to oraFine)
                        }
                    }

                    listaOccupati.add(giornoCorrente to slotOrari)
                }
            }

            Log.d("ListaGiorniViewModel", "Chiamata a Firestore per ottenere i giorni occupati")

        } catch (e: Exception) {
            Log.e("ListaGiorniViewModel", "Errore durante l'accesso a Firestore: ${e.message}", e)
        }

        Log.d("Sorted", listaOccupati.toString())


        return listaOccupati
    }


    private fun aggiornaListaOccupati(
        listaOrari: List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>,
        listaOccupati: List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>
    ): List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>> {

        // Crea una nuova lista modificata a partire dalla listaOrari
        return listaOrari.map { (data, orariDisponibili) ->

            //Trova, se esiste una corrispondenza nella listaOccupati per la data corrente, tutti gli orari occupati del giorno corrente
            val occupatiPerQuestaData = listaOccupati.find { it.first == data }?.second


            //Se ci sono orari occupati per questa data, li rimuovo da orariDisponibili
            val orariAggiornati = if (occupatiPerQuestaData != null) {
                orariDisponibili.filterNot { orarioDisponibile ->
                    occupatiPerQuestaData.any { orarioOccupato ->
                        orarioDisponibile == orarioOccupato
                    }
                }
            } else {
                Log.d("ListaGiorniViewModel", "Diverso")
                orariDisponibili
            }
            Log.d("Stampa", data.toString())
            Log.d("Stampa", orariAggiornati.toString())



            //Data con la nuova lista di orari aggiornati
            data to orariAggiornati
        }
    }


    fun aggiornaSlotdaSessanta(
        lista: List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>
    ): List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>> {

        return lista.map { (data, orari) ->
            val nuoviOrari = mutableListOf<Pair<LocalTime, LocalTime>>()

            for (i in 0 until orari.size - 1) {
                // Ogni intervallo di 1 ora è formato dal primo elemento dell'elemento corrente e il secondo del successivo
                val orarioCorrente = orari[i]
                val orarioSuccessivo = orari[i+1]

                if(orarioCorrente.second == orarioSuccessivo.first){
                    val start = orari[i].first
                    val end = orari[i + 1].second
                    nuoviOrari.add(Pair(start, end))
                }


            }

            Pair(data, nuoviOrari)
        }
    }

}