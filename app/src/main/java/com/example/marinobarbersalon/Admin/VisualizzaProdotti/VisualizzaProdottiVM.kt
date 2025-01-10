package com.example.marinobarbersalon.Admin.VisualizzaProdotti

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
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
    //per l'img
    private val storageReference = FirebaseStorage.getInstance().reference
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

    //Connessione con Supabase
    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://dboogadeyqtgiirwnopm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRib29nYWRleXF0Z2lpcndub3BtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY0NTMyMDQsImV4cCI6MjA1MjAyOTIwNH0.5AERHBZ3WTKr9KOzTNQRWp-xCgNserTU1j1dyJTpIMY"
    ) {
        install(Storage) //Installazione del modulo Storage
    }



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

    fun deleteProdotto(prodotto: Prodotto){
        viewModelScope.launch {
            try {
                FirebaseFirestore.getInstance()
                    .collection("prodotti")
                    .document(prodotto.id)
                    .delete()
                    .await()
                _prodottiState.value = _prodottiState.value.filter { it.id != prodotto.id }
                //Log.d("prodotti", "$prodotto.id")
            } catch (e: Exception) {
                Log.e("VisualizzaServiziVM", "Errore durante l'eliminazione del servizio: ${prodotto.id}", e)
            }
        }
    }








    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //FUNZIONI PER LA TERZA PAGINA
    //----------------------------------------------------------------------------------------------
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

//    fun aggiungiProdotto(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
//        if (!validateForm()) {
//            Log.d("prodotti", "Form non valido, impossibile aggiungere il prodotto.")
//            return
//        }
//
//        val prodotto = Prodotto(
//            nome = _nome.value,
//            descrizione = _descrizione.value,
//            categoria = _categoria.value,
//            prezzo = _prezzo.value,
//            quantita = _quantita.value,
//            immagine = _immagine.value
//        )
//
//        viewModelScope.launch {
//            try {
//                firestore.collection("prodotti").add(prodotto).await()
//                onSuccess()
//                resetFields()
//            } catch (e: Exception) {
//                onError(e)
//            }
//        }
//    }

    fun aggiungiProdottoWithImage(
        context: Context,
        imageUri: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("aggiungiProdotto", "Inizio creazione prodotto su Firestore")

                //Step 1: Creazione prodotto su Firestore
                val prodotto = Prodotto(
                    nome = _nome.value,
                    descrizione = _descrizione.value,
                    categoria = _categoria.value,
                    prezzo = _prezzo.value,
                    quantita = _quantita.value,
                    immagine = "" //L'img sarà aggiornata dopo
                )

                val prodottoRef = firestore.collection("prodotti").add(prodotto).await()
                val prodottoId = prodottoRef.id

                Log.d("aggiungiProdotto", "Prodotto creato con ID: $prodottoId")

                //Step 2: Caricamento immagine su Supabase
                Log.d("aggiungiProdotto", "Inizio caricamento immagine su Supabase")
                val publicUrl = uploadImageToSupabase(context, imageUri, prodottoId)

                Log.d("aggiungiProdotto", "Immagine caricata con URL: $publicUrl")

                //Step 3: Aggiornamento Firestore con l'URL dell'immagine
                Log.d("aggiungiProdotto", "Inizio aggiornamento immagine su Firestore")
                prodottoRef.update("immagine", publicUrl).await()

                Log.d("aggiungiProdotto", "Prodotto aggiornato con URL immagine su Firestore")

                onSuccess()
            } catch (e: Exception) {
                Log.e("aggiungiProdotto", "Errore durante l'aggiunta del prodotto: ${e.message}", e)
                onError(e)
            }
        }
    }

    private suspend fun uploadImageToSupabase(context: Context, imageUri: String, prodottoId: String): String {
        val inputStream = context.contentResolver.openInputStream(android.net.Uri.parse(imageUri))
            ?: throw IllegalArgumentException("Impossibile aprire il file URI: $imageUri")

        val storage = supabaseClient.storage

        try {
            //Carica il file nella sottocartella "photos/photos"
            val path = storage.from("photos")
                .upload("photos/$prodottoId.jpg", inputStream.readBytes())

            //Genera l'URL pubblico per il file
            val publicUrl = "https://dboogadeyqtgiirwnopm.supabase.co/storage/v1/object/public/photos/photos/$prodottoId.jpg"
            Log.d("uploadImage", "URL immagine generato: $publicUrl")
            return publicUrl
        } catch (e: Exception) {
            Log.e("uploadImage", "Errore durante il caricamento dell'immagine su Supabase: ${e.message}", e)
            throw Exception("Errore durante il caricamento dell'immagine su Supabase: ${e.message}", e)
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
    //----------------------------------------------------------------------------------------------
}