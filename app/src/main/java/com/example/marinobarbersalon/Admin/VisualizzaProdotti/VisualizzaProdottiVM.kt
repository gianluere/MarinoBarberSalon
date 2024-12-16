package com.example.marinobarbersalon.Admin.VisualizzaProdotti

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class VisualizzaProdottiVM : ViewModel(){

    //----------------------------------------------------------------------------------------------
    // PER LA PRIMA PAGINA
    private val firestore = FirebaseFirestore.getInstance()

    private val _categoriaSelezionata = MutableStateFlow<String>("")
    val categoriaSelezionata: StateFlow<String> = _categoriaSelezionata
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // PER LA SECONDA PAGINA
    private val _prodottiState = MutableStateFlow<List<Prodotto>>(emptyList())
    val prodottiState: StateFlow<List<Prodotto>> = _prodottiState.asStateFlow()
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    // PER LA TERZA PAGINA
    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome.asStateFlow()

    private val _descrizione = MutableStateFlow("")
    val descrizione: StateFlow<String> = _descrizione.asStateFlow()

    private val _categoria = MutableStateFlow("")
    val categoria: StateFlow<String> = _categoria.asStateFlow()

    private val _prezzo = MutableStateFlow(0.0)
    val prezzo: StateFlow<Double> = _prezzo.asStateFlow()

    private val _quantita = MutableStateFlow(0)
    val quantita: StateFlow<Int> = _quantita.asStateFlow()

    private val _immagine = MutableStateFlow("")
    val immagine: StateFlow<String> = _immagine.asStateFlow()

    //validazione form
    private val _formErrors = MutableStateFlow<List<String>>(emptyList())
    val formErrors: StateFlow<List<String>> = _formErrors.asStateFlow()

//    // Stato per l'aggiunta
//    private val _aggiuntaStato = MutableStateFlow<AggiuntaStato>(AggiuntaStato.Inattivo)
//    val aggiuntaStato: StateFlow<AggiuntaStato> = _aggiuntaStato.asStateFlow()
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // FUNZIONI SECONDA PAGINA

    fun onCategoriaSelezionata(categoria: String) {
        _categoriaSelezionata.value = categoria
        fetchProdottiPerCategoria(categoria)
    }

    fun resetCategoria() {
        _categoriaSelezionata.value = ""
    }

    fun fetchProdottiPerCategoria(categoria: String) {
        viewModelScope.launch {
            try {
                val prodotti = getProdottiFromFirestore(categoria)
                _prodottiState.value = prodotti
                Log.d("VisualizzaProdottiVM", "Prodotti ottenuti in _prodottiState: ${_prodottiState.value}")
                Log.d("VisualizzaProdottiVM", "Prodotti ottenuti in prodottiState: ${prodottiState.value}")
            } catch (e: Exception) {
                Log.e("VisualizzaProdottiVM", e.toString())
            }
        }
    }




    private suspend fun getProdottiFromFirestore(categoria: String): List<Prodotto> {
        val db = firestore.collection("prodotti")
        val snapshot = db
            .whereEqualTo("categoria", categoria)
            .get()
            .await()
        Log.d("VisualizzaProdottiVM", "Prodotti ottenuti: ${snapshot.documents}")


        return snapshot.documents.mapNotNull { document ->
            document.toObject(Prodotto::class.java)?.copy(id = document.id)
        }

    }
    fun increaseStock(prodotto: Prodotto) {
        viewModelScope.launch {
            try {
                val newStock = prodotto.quantita + 1
                firestore.collection("prodotti")
                    .document(prodotto.id)
                    .update("quantita", newStock)
                    .await()

                // Aggiorna lo stato con il nuovo prodotto
                val updatedProdotti = _prodottiState.value.map {
                    if (it.id == prodotto.id) it.copy(quantita = newStock) else it
                }
                _prodottiState.value = updatedProdotti
            } catch (e: Exception) {
                Log.e("VisualizzaProdottiVM", "Errore nell'aggiornamento stock: ${e.message}")
            }
        }
    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //FUNZIONI PER LA TERZA PAGINA
    fun onNomeChange(newNome: String) {
        _nome.value = newNome
        validateForm()
    }

    fun onDescrizioneChange(newDescrizione: String) {
        _descrizione.value = newDescrizione
        validateForm()
    }

    fun onCategoriaChange(newCategoria: String) {
        _categoria.value = newCategoria
        validateForm()
    }

    fun onPrezzoChange(newPrezzo: Double) {
        _prezzo.value = newPrezzo
        validateForm()
    }

    fun onQuantitaChange(newQuantita: Int) {
        _quantita.value = newQuantita
        validateForm()
    }

    fun onImmagineChange(newImmagine: String) {
        _immagine.value = newImmagine
        validateForm()
    }

    fun setCategoria(categoria : String){
        _categoria.value = categoria
    }

    fun validateForm(): Boolean {
        val errors = mutableListOf<String>()

        if (_nome.value.isEmpty()) errors.add("Il nome non può essere vuoto.")
        if (_descrizione.value.isEmpty()) errors.add("La descrizione non può essere vuota.")
        if (_categoria.value.isEmpty()) errors.add("La categoria non può essere vuota.")
        if (_prezzo.value <= 0) errors.add("Il prezzo deve essere maggiore di zero.")
        if (_quantita.value <= 0) errors.add("La quantità deve essere maggiore di zero.")
        if (_immagine.value.isEmpty()) errors.add("L'immagine non può essere vuota.")

        _formErrors.value = errors
        return errors.isEmpty()
    }

    fun aggiungiProdotto(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        if (!validateForm()) {
            Log.d("prodotti", "Form non valido, impossibile aggiungere il prodotto.")
            return
        }

        val prodotto = Prodotto(
            nome = _nome.value,
            descrizione = _descrizione.value,
            categoria = _categoria.value,
            prezzo = _prezzo.value,
            quantita = _quantita.value,
            immagine = _immagine.value
        )

        viewModelScope.launch {
            try {
                firestore.collection("prodotti").add(prodotto).await()
                onSuccess()
                resetFields()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun resetFields() {
        _nome.value = ""
        _descrizione.value = ""
        //_categoria.value = ""
        _prezzo.value = 0.0
        _quantita.value = 0
        _immagine.value = ""
    }















}