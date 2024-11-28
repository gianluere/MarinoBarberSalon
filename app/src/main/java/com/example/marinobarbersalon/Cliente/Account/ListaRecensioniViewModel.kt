package com.example.marinobarbersalon.Cliente.Account

import androidx.compose.runtime.tooling.CompositionGroup
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaRecensioniViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val userEmail = FirebaseAuth.getInstance().currentUser!!.email

    private val _listaRecensioni = MutableStateFlow(listOf<Recensione>())
    val listaRecensioni : StateFlow<List<Recensione>> = _listaRecensioni.asStateFlow()


    init {
        caricaListaRecensioni()
    }

    fun caricaListaRecensioni(){
        db.collection("recensioni")
            .get()
            .addOnSuccessListener { result->
                _listaRecensioni.value = result.toObjects()
            }
    }

    fun inserisciRecensione(
        recensione : Recensione,
        onCompleted : () -> Unit
    ){
        db.collection("recensioni").add(recensione)
            .addOnSuccessListener {
                onCompleted()
            }
    }

}