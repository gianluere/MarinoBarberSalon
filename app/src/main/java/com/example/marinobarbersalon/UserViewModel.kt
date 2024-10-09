package com.example.marinobarbersalon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow(User())
    val userState : StateFlow<User> = _userState.asStateFlow()
    private val db = Firebase.firestore

    init {
        checkAuthState()

    }

    fun checkAuthState(){
        if (auth.currentUser == null){
            _userState.value.state = AuthState.Unauthenticated
        }else{
            _userState.value.state = AuthState.Authenticated
            caricaDati()
        }
    }

    fun login(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _userState.value= _userState.value.copy(state = AuthState.Error("Email e password non possono essere vuoti"))
            return
        }
        _userState.value.state = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                }else{
                    _userState.value = _userState.value.copy(state = AuthState.Error(task.exception?.message?:"Errore"))
                }
            }
    }

    fun signup(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _userState.value = _userState.value.copy(state = AuthState.Error("Email e password non possono essere vuoti"))
            return
        }
        _userState.value.state = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                }else{
                    _userState.value = _userState.value.copy(state = AuthState.Error(task.exception?.message?:"Errore"))
                }
            }
    }

    fun logout(){
        auth.signOut()
        _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)

    }

    fun caricaDati(){

        auth.currentUser?.email?.let {

            db.collection("utenti").document(it)
                .get()
                .addOnSuccessListener {document ->
                    val dati : UserFirebase? = document.toObject(UserFirebase::class.java)
                    Log.d("USERR", "cosa")
                    if (dati != null) {
                        _userState.value = _userState.value.copy(
                            nome = dati.nome,
                            cognome = dati.cognome,
                            eta = dati.eta,
                            email = auth.currentUser?.email,
                            telefono = dati.telefono,
                            password = auth.currentUser!!.uid
                        )
                    }
                }
                .addOnFailureListener {

                }
        }
    }

    fun aggiungiApp(
        servizio : String,
        orarioInizio : String,
        orarioFine : String,
        dataSel : String
    ){
        viewModelScope.launch {
            aggiungiAppuntamento(servizio,
                orarioInizio,
                orarioFine,
                dataSel)
        }
    }

    suspend fun aggiungiAppuntamento(
        servizio : String,
        orarioInizio : String,
        orarioFine : String,
        dataSel : String
    ){
        val data = dataSel.replace("/", "-")
        Log.d("Appuntamento", servizio)
        var idServizio : String = ""
        val results = db.collection("servizi").whereEqualTo("nome", servizio).limit(1)
            .get().await()

        idServizio = results.documents[0].id


        Log.d("Appuntamento", idServizio)
        val servizioRiferimento : DocumentReference = db.collection("servizi").document(idServizio)
        val utenteRiferimento : DocumentReference = db.collection("utenti").document(_userState.value.email.toString())

        val appuntamento = hashMapOf(
            "cliente" to utenteRiferimento,
            "orarioInizio" to orarioInizio,
            "orarioFine" to orarioFine,
            "data" to data,
            "servizio" to servizioRiferimento
        )

        val appuntamentoPath = db.collection("appuntamenti").document(data)

        appuntamentoPath.get()
            .addOnSuccessListener{ documento ->
                if (documento.exists()){
                    Log.d("Appuntamento", "primo esiste")
                    appuntamentoPath.collection("app").document("$orarioInizio-$orarioFine")
                        .set(appuntamento)
                        .addOnSuccessListener {
                            Log.d("Appuntamento", "Secondo esiste")
                            db.collection("occupati").document(data).get()
                                .addOnSuccessListener {
                                    if (it.exists()){
                                        db.collection("occupati").document(data).update(
                                            hashMapOf(
                                                orarioInizio to "occupato",
                                                orarioFine to "occupato"
                                            ) as Map<String, Any>
                                        )
                                    }
                                }
                        }
                }else{
                    appuntamentoPath.set(emptyMap<String, Any>()).addOnSuccessListener {
                        appuntamentoPath.collection("app")
                            .document("$orarioInizio-$orarioFine").set(appuntamento)
                            .addOnSuccessListener {
                                db.collection("occupati").document(data)
                                    .get()
                                    .addOnSuccessListener{
                                        if (it.exists()){
                                            db.collection("occupati").document(data).update(
                                                hashMapOf(
                                                    orarioInizio to "occupato",
                                                    orarioFine to "occupato"
                                                ) as Map<String, Any>
                                            )
                                        }else{
                                            db.collection("occupati").document(data).set(hashMapOf(
                                                orarioInizio to "occupato",
                                                orarioFine to "occupato"
                                            ))
                                        }
                                    }


                            }

                    }
                }
            }
    }


}

