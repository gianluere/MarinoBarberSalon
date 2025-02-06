package com.example.marinobarbersalon.Cliente.Home

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaServiziViewModel : ViewModel() {

    private var _listaServizi = MutableStateFlow(listOf<Servizio>())
    val listaServizi : StateFlow<List<Servizio>> = _listaServizi.asStateFlow()

    private val db = Firebase.firestore

    init {
        getListaServizi()
    }

    //carica la lista dei servizi
    fun getListaServizi(){

        db.collection("servizi")
            .get()
            .addOnSuccessListener {result ->
                _listaServizi.value = result.toObjects()
                _listaServizi.value.forEach {servizio ->
                    servizio.descrizione = servizio.descrizione?.replace(Regex("\\s+"), " ")
                }
            }
            .addOnFailureListener {

            }


    }

}