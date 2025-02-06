package com.example.marinobarbersalon.Cliente.Shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListaProdottiViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _listaProdotti = MutableStateFlow(listOf<Prodotto>())
    val listaProdotti : StateFlow<List<Prodotto>> = _listaProdotti.asStateFlow()

    private val _prodotto = MutableStateFlow(Prodotto())
    val prodotto : StateFlow<Prodotto> = _prodotto.asStateFlow()

    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://dboogadeyqtgiirwnopm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRib29nYWRleXF0Z2lpcndub3BtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY0NTMyMDQsImV4cCI6MjA1MjAyOTIwNH0.5AERHBZ3WTKr9KOzTNQRWp-xCgNserTU1j1dyJTpIMY"
    ) {
        install(Storage)
    }

    private val _fotoscelta = MutableStateFlow("")
    val fotoscelta : StateFlow<String> = _fotoscelta.asStateFlow()

    //Carico la lista filtrata per categoria dei prodotti (Barba, Capelli o Viso)
    fun caricaListaProdotti(categoria : String){
        db.collection("prodotti")
            .get()
            .addOnSuccessListener { result->
                var lista : List<Prodotto> = result.toObjects()

                lista = lista.filter {
                    it.categoria == categoria.lowercase(Locale.ROOT)
                }

                lista = lista.sortedBy {
                    it.nome
                }

                _listaProdotti.value = lista
            }
    }

    private suspend fun trovaProd(nome : String) : Prodotto{
        val prodotti = db.collection("prodotti")
            .whereEqualTo("nome", nome)
            .get().await()


        return prodotti.toObjects<Prodotto>()[0]

    }

    fun trovaProdotto(nome : String){
        viewModelScope.launch {
            _prodotto.value = trovaProd(nome)
        }
    }

    private suspend fun prenProdotto(
        prodotto: Prodotto,
        quantita: Int,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        try {
            // Prendo i riferimenti ai documenti
            val prod = db.collection("prodotti")
                .whereEqualTo("nome", prodotto.nome)
                .limit(1)
                .get()
                .await()

            if (prod.isEmpty) {
                Log.e("Prod", "Prodotto non trovato")
                onFailed()
                return
            }

            val prodRef = db.collection("prodotti").document(prod.documents[0].id)
            val userRef = db.collection("utenti").document(auth.currentUser?.email.toString())

            val data = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(Date())

            val prenotazioneProd = hashMapOf(
                "prodotto" to prodRef,
                "utente" to userRef,
                "quantita" to quantita,
                "stato" to "attesa",
                "data" to data
            )

            //Transazione per verificare la quantità prima di aggiornare
            db.runTransaction { transaction ->
                val userSnapshot = transaction.get(userRef)
                val prodottoSnapshot = transaction.get(prodRef)

                if (!prodottoSnapshot.exists()) {
                    throw FirebaseFirestoreException("Prodotto non trovato", FirebaseFirestoreException.Code.NOT_FOUND)
                }

                val quantitaDisponibile = prodottoSnapshot.getLong("quantita") ?: 0

                if (quantitaDisponibile < quantita) {
                    throw FirebaseFirestoreException("Quantità insufficiente", FirebaseFirestoreException.Code.ABORTED)
                }

                //Aggiorno la quantità solo se è sufficiente
                transaction.update(prodRef, "quantita", quantitaDisponibile - quantita)

                //Aggiungo il documento alla collezione prodottiPrenotati
                if (userSnapshot.exists()) {
                    val nuovoDocumentoRef = db.collection("prodottiPrenotati").document()
                    transaction.set(nuovoDocumentoRef, prenotazioneProd)
                    Log.d("Prod", "Inserito documento")
                }

                onSuccess()

            }.await()

        } catch (e: FirebaseFirestoreException) {
            Log.e("Prod", "Errore nella prenotazione: ${e.message}")
            onFailed()
        } catch (e: Exception) {
            Log.e("Prod", "Errore generico: ${e.message}")
            onFailed()
        }
    }



    fun prenotaProdotto(prodotto: Prodotto, quantita: Int, onSuccess : () -> Unit, onFailed: () -> Unit){
        viewModelScope.launch {
            prenProdotto(prodotto, quantita, onSuccess, onFailed)
        }
    }

    fun getSignedUrl(filePath: String, expiresInSeconds: Int = 3600) : String {
        val storage = supabaseClient.storage
        val bucket = storage.from("photos") // Nome del tuo bucket

        return bucket.publicUrl("photos/$filePath")



    }








}