package com.example.marinobarbersalon.Cliente.Account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
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

    private val _notifiche = MutableStateFlow(0)
    val notifiche : StateFlow<Int> = _notifiche.asStateFlow()


    init {

        startListener()

    }


    private fun startListener(){

        if (userEmail != null) {
            db.collection("utenti").document(userEmail).addSnapshotListener{snapshot, error ->

                if (error != null) {
                    Log.e("FirestoreError", "Errore nel ricevere aggiornamenti: ", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val appuntamentiList = snapshot.get("appuntamenti") as? List<DocumentReference>
                    if (appuntamentiList != null) {
                        Log.d("Notif", appuntamentiList.toString())
                        Log.d("Notif", "AppList: " + appuntamentiList.size.toString())
                    }

                    if (appuntamentiList != null) {
                        viewModelScope.launch {
                            //val app = async { recuperaDocumenti(appuntamentiList) }
                            val appuntamenti = recuperaDocumenti(appuntamentiList)
                            //val appuntamenti = app.await()
                            Log.d("Notif", "Appuntamentiveri: " + appuntamenti.size.toString())
                            val oggi = LocalDate.now()
                            val ora = LocalTime.now()
                            var totale = 0

                            for (appuntamento in appuntamenti){
                                val giornoApp = LocalDate.parse(appuntamento.data, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                val oraApp = LocalTime.of(appuntamento.orarioInizio.take(2).toInt(), appuntamento.orarioInizio.substring(3, 5).toInt())
                                if (oggi.isBefore(giornoApp)){
                                    totale += 1
                                }else if (oggi.isEqual(giornoApp)){
                                    if (ora.isBefore(oraApp)){
                                        totale += 1
                                    }
                                }
                            }

                            _notifiche.value = totale
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


    private suspend fun recuperaDocumenti(listaAppuntamenti : List<DocumentReference>): List<Appuntamento>{
        val appuntamenti = mutableListOf<Appuntamento>()
        Log.d("Notif", "size: " + listaAppuntamenti.size.toString())
        for (document in listaAppuntamenti){
            val result = document.get().await()

            result.toObject(Appuntamento::class.java)?.let { appuntamenti.add(it)
                Log.d("Notif", "Ci sto " + result.id)}
        }


        Log.d("Notif", "Lunghezza funzione: " + appuntamenti.size.toString())
        Log.d("Notif", "Appuntamenti funzione: " + appuntamenti.toString())
        return appuntamenti
    }


}