package com.example.marinobarbersalon.Cliente.Shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class ListaProdottiViewModel : ViewModel() {

    private val db = Firebase.firestore

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



}