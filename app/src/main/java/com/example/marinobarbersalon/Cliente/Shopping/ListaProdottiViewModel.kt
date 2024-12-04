package com.example.marinobarbersalon.Cliente.Shopping

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaProdottiViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _listaProdotti = MutableStateFlow(listOf<Prodotto>())
    val listaProdotti : StateFlow<List<Prodotto>> = _listaProdotti.asStateFlow()

    fun caricaListaProdotti(){
        db.collection("recensioni")
            .get()
            .addOnSuccessListener { result->
                _listaProdotti.value = result.toObjects()
            }
    }

}