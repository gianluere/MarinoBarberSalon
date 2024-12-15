package com.example.marinobarbersalon.Cliente.Shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class ListaProdottiViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _listaProdotti = MutableStateFlow(listOf<Prodotto>())
    val listaProdotti : StateFlow<List<Prodotto>> = _listaProdotti.asStateFlow()

    private val _prodotto = MutableStateFlow(Prodotto())
    val prodotto : StateFlow<Prodotto> = _prodotto.asStateFlow()


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
        prodotto : Prodotto,
        quantita : Int,
        onSuccess : () -> Unit,
        onFailed : () -> Unit
    ){

        val prod = db.collection("prodotti")
            .whereEqualTo("nome", prodotto.nome).limit(1).get().await()

        val prod_ref = db.collection("prodotti").document(prod.documents[0].id)
        val user_ref = db.collection("utenti").document(auth.currentUser?.email.toString())

        val prenotazioneProd = hashMapOf(
            "prodotto" to prod_ref,
            "utente" to user_ref,
            "quantita" to quantita,
            "stato" to "attesa"
        )

        try{
            db.runTransaction {transaction ->
                val userSnapshot = transaction.get(user_ref)
                val prodottoSnapshot = transaction.get(prod_ref)

                if (prodottoSnapshot.exists()){
                    transaction.update(prod_ref, "quantita", prodotto.quantita-quantita)
                    Log.d("Prod", "Aggiornata quantita: " + (prodotto.quantita-quantita).toString())
                }

                if (userSnapshot.exists()){
                    val nuovoDocumentoRef = db.collection("prodottiPrenotati").document()
                    transaction.set(nuovoDocumentoRef, prenotazioneProd)
                    Log.d("Prod", "Inserito documento")
                    //transaction.set(db.collection("prodottiPrenotati"), prenotazioneProd)
                }

                onSuccess()

            }.await()
        }catch (e: FirebaseFirestoreException){
            onFailed()
        }


    }


    fun prenotaProdotto(prodotto: Prodotto, quantita: Int, onSuccess : () -> Unit, onFailed: () -> Unit){
        viewModelScope.launch {
            prenProdotto(prodotto, quantita, onSuccess, onFailed)
        }
    }




}