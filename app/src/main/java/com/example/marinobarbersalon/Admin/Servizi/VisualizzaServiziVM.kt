package com.example.marinobarbersalon.Admin.Servizi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Home.Servizio
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class VisualizzaServiziVM: ViewModel(){

    private val firestore = FirebaseFirestore.getInstance()

    private val _serviziState = MutableStateFlow<List<Servizio>>(emptyList())
    val serviziState: StateFlow<List<Servizio>> = _serviziState.asStateFlow()

    //------------------------------------------------------------------------------
    // PER LA SECONDA PAGINA
    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome

    private val _descrizione = MutableStateFlow("")
    val descrizione: StateFlow<String> = _descrizione

    private val _tipo = MutableStateFlow("Capelli") // di def viene messo su capelli
    val tipo: StateFlow<String> = _tipo

    private val _durata = MutableStateFlow(0)
    val durata: StateFlow<Int> = _durata

    private val _prezzo = MutableStateFlow(0.00)
    val prezzo: StateFlow<Double> = _prezzo



    //------------------------------------------------------------------------------------------------------------------
    // PER LA PRIMA PAGINA
    fun fetchServizi() {
        viewModelScope.launch {
            try {

                val servizi = getServiziFromFirestore()
                _serviziState.value = servizi
            } catch (e: Exception) {
                Log.e("VisualizzaServiziVM", e.toString())
            }
        }
    }

    private suspend fun getServiziFromFirestore(): List<Servizio> {
        val db = firestore.collection("servizi")
        val snapshot = db.get().await()


        return snapshot.documents.mapNotNull { document ->
            document.toObject(Servizio::class.java)?.copy(id = document.id)
        }
    }

    fun deleteServizio(servizio: Servizio) {
        viewModelScope.launch {
            try {
                FirebaseFirestore.getInstance()
                    .collection("servizi")
                    .document(servizio.id)
                    .delete()
                    .await()
                //Log.d("servizi", "$servizio.id")
            } catch (e: Exception) {
                Log.e("VisualizzaServiziVM", "Errore durante l'eliminazione del servizio: ${servizio.id}", e)
            }
        }

    }


    //------------------------------------------------------------------------------------------------------------------
    // PER LA SECONDA PAGNA

    fun onNomeChange(newNome: String) {
        _nome.value = newNome
    }

    fun onDescrizioneChange(newDescrizione: String) {
        _descrizione.value = newDescrizione
    }

    fun onTipoChange(newTipo: String) {
        _tipo.value = newTipo
    }

    fun onDurataChange(newDurata: Int) {
        _durata.value = newDurata
    }

    fun onPrezzoChange(newPrezzo: Double) {
        _prezzo.value = newPrezzo
    }

    // Funzione per aggiungere il servizio
    fun aggiungiServizio(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val servizio = Servizio(
            nome = _nome.value,
            descrizione = _descrizione.value,
            tipo = _tipo.value,
            durata = _durata.value,
            prezzo = _prezzo.value
        )
        viewModelScope.launch {
            try {
                firestore.collection("servizi").add(servizio).await()
                onSuccess()
                Log.d("servizi", "${servizio.id}")
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun resetFields() {
        _nome.value = ""
        _descrizione.value = ""
        _durata.value = 0
        _prezzo.value = 0.00
        _tipo.value = ""
    }




}