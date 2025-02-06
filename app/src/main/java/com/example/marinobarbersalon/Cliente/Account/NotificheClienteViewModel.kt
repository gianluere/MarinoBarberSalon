package com.example.marinobarbersalon.Cliente.Account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoPrenotato
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NotificheClienteViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val userEmail = FirebaseAuth.getInstance().currentUser!!.email

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _notifichePrenotazioni = MutableStateFlow(0)
    val notifichePrenotazioni : StateFlow<Int> = _notifichePrenotazioni.asStateFlow()

    private val _notificheProdotti = MutableStateFlow(0)
    val notificheProdotti : StateFlow<Int> = _notificheProdotti.asStateFlow()


    init {

        startListenerPrenotazioni()
        startListenerAcquisti()

    }

    //Listener automatico per aggiornare i valori delle notifiche per la bottomBar
    private fun startListenerPrenotazioni(){

        if (userEmail != null) {
            db.collection("utenti").document(userEmail).addSnapshotListener{snapshot, error ->

                if (error != null) {
                    Log.e("FirestoreError", "Errore nel ricevere aggiornamenti: ", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val appuntamentiList = snapshot.get("appuntamenti") as? List<DocumentReference>


                    if (appuntamentiList != null) {
                        viewModelScope.launch {

                            val appuntamenti = recuperaDocumenti(appuntamentiList)


                            val oggi = LocalDate.now()
                            val ora = LocalTime.now()
                            var totale = 0

                            for (appuntamento in appuntamenti){
                                val giornoApp = LocalDate.parse(appuntamento.data, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                //è HH:mm quindi devo saltare il :
                                val oraApp = LocalTime.of(appuntamento.orarioInizio.take(2).toInt(), appuntamento.orarioInizio.substring(3, 5).toInt())
                                if (oggi.isBefore(giornoApp)){
                                    totale += 1
                                }else if (oggi.isEqual(giornoApp)){
                                    //aggiungo solo se l'orario dell'appuntamento non è ancora arrivato
                                    if (ora.isBefore(oraApp)){
                                        totale += 1
                                    }
                                }
                            }

                            _notifichePrenotazioni.value = totale
                        }
                    } else {
                        Log.e("FirestoreError", "Nessun appuntamento trovato per l'utente.")

                    }
                } else {
                    Log.e("FirestoreError", "Il documento dell'utente non esiste.")

                }
            }

        }

    }


    //Funzione di appoggio che converte i DocRef in oggetti Appuntamento
    private suspend fun recuperaDocumenti(listaAppuntamenti : List<DocumentReference>): List<Appuntamento>{
        val appuntamenti = mutableListOf<Appuntamento>()
        Log.d("Notif", "size: " + listaAppuntamenti.size.toString())
        for (document in listaAppuntamenti){
            val result = document.get().await()

            result.toObject(Appuntamento::class.java)?.let { appuntamenti.add(it) }
        }


        return appuntamenti
    }


    //Listener automatico per aggiornare i valori delle notifiche per la bottomBar
    private fun startListenerAcquisti(){

        if (userEmail != null) {
            val userReference = auth.currentUser?.email?.let { db.collection("utenti").document(it) }

            db.collection("prodottiPrenotati").whereEqualTo("utente", userReference)
                .whereEqualTo("stato", "attesa").addSnapshotListener{snapshot, error ->

                if (error != null) {
                    Log.e("FirestoreError", "Errore nel ricevere aggiornamenti: ", error)
                    return@addSnapshotListener
                }

                    val prodottiList = snapshot?.toObjects(ProdottoPrenotato::class.java) ?: emptyList()

                    Log.d("ProdPren", prodottiList.size.toString())

                    _notificheProdotti.value = prodottiList.size

            }

        }

    }


}