package com.example.marinobarbersalon.Cliente.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class UserViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow(User())
    val userState : StateFlow<User> = _userState.asStateFlow()
    private val db = Firebase.firestore

    private val _listaAppuntamenti = MutableStateFlow(listOf<Appuntamento>())
    val listaAppuntamenti : StateFlow<List<Appuntamento>> = _listaAppuntamenti.asStateFlow()

    init {
        checkAuthState()

    }

    private fun checkAuthState(){
        if (auth.currentUser == null){
            _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)
        }else{
            checkIfCliente(auth.currentUser?.email ?: "") { isCliente ->
                if (isCliente) {
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                    caricaDati()
                    sincronizzaPrenotazioni()
                } else {
                    // Se non è un admin, non lo autentichiamo
                    _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)
                }
            }
        }
    }

    private fun checkIfCliente(email: String, callback: (Boolean) -> Unit) {
        // Funzione per verificare se l'email è associata a un amministratore
        db.collection("utenti").document(email).get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun login(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _userState.value= _userState.value.copy(state = AuthState.Error("Email e password non possono essere vuoti"))
            return
        }
        _userState.value = _userState.value.copy(state = AuthState.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                    caricaDati()
                }else{
                    _userState.value = _userState.value.copy(state = AuthState.Error(
                        task.exception?.message ?: "Errore"
                    )
                    )
                }
            }
    }

    fun signup(email : String, password : String, nome: String, cognome : String, eta: Int, telefono : String){

        if (email.isEmpty() || password.isEmpty()){
            _userState.value = _userState.value.copy(state = AuthState.Error("Email e password non possono essere vuoti"))
            return
        }
        _userState.value = _userState.value.copy(state = AuthState.Loading)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    db.collection("utenti").document(auth.currentUser?.email.toString())
                        .set(
                            hashMapOf(
                                "nome" to nome,
                                "cognome" to cognome,
                                "email" to email,
                                "eta" to eta,
                                "telefono" to telefono
                            )
                        ).addOnSuccessListener {
                            caricaDati()
                            _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                        }

                }else{
                    _userState.value = _userState.value.copy(state = AuthState.Error(
                        task.exception?.message ?: "Errore"
                    )
                    )
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
                            password = auth.currentUser!!.uid,
                            appuntamenti = dati.appuntamenti
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
        dataSel : String,
        onSuccess : () -> Unit,
        onFailed : () -> Unit
    ){
        viewModelScope.launch {
            Log.d("Riuscito", "Inizio: " + _userState.value.appuntamenti.size.toString())
            aggiungiAppuntamento(servizio,
                orarioInizio,
                orarioFine,
                dataSel,
                onSuccess,
                onFailed)

            val docUser = db.collection("utenti").document(auth.currentUser!!.email.toString())
                .get().await()
            val dati = docUser.toObject(UserFirebase::class.java)
            if (dati != null) {
                Log.d("Riuscito", "DimDatiApp: " + dati.appuntamenti.size)
            }
            if (dati != null){
                _userState.value = _userState.value.copy(
                    appuntamenti = dati.appuntamenti
                )
                Log.d("Riuscito", "Fine: " + _userState.value.appuntamenti.size.toString())
            }
        }
    }

    private suspend fun aggiungiAppuntamento(
        servizio: String,
        orarioInizio: String,
        orarioFine: String,
        dataSel: String,
        onSuccess : () -> Unit,
        onFailed : () -> Unit
    ) {
        val data = dataSel.replace("/", "-")
        Log.d("Appuntamento", servizio)
        var idServizio: String = ""
        val results = db.collection("servizi").whereEqualTo("nome", servizio).limit(1)
            .get().await()

        //idServizio = results.documents[0].id
        val servizio : String = results.documents[0].get("nome").toString()
        val descrizione : String = results.documents[0].get("descrizione").toString()
        val prezzo : Double = results.documents[0].get("prezzo").toString().toDouble()

        Log.d("Appuntamento", idServizio)
        //val servizioRiferimento: DocumentReference = db.collection("servizi").document(idServizio)
        val utenteRiferimento: DocumentReference = db.collection("utenti").document(_userState.value.email.toString())

        val appuntamento = hashMapOf(
            "cliente" to utenteRiferimento,
            "orarioInizio" to orarioInizio,
            "orarioFine" to orarioFine,
            "data" to data,
            "servizio" to servizio,
            "descrizione" to descrizione,
            "prezzo" to prezzo
        )

        val appuntamentoPath = db.collection("appuntamenti").document(data)
        val occupatiPath = db.collection("occupati").document(data)

        try {
            // Esegui una transazione per garantire che solo una scrittura avvenga alla volta
            db.runTransaction { transaction ->
                val appuntamentoSnapshot = transaction.get(appuntamentoPath)
                val occupatiSnapshot = transaction.get(occupatiPath)
                val userSnapshot = transaction.get(utenteRiferimento)
                val chiave = "$orarioInizio-$orarioFine"

                if (appuntamentoSnapshot.exists()) {
                    Log.d("Appuntamento", "Documento appuntamento già esistente")
                    // Aggiorna il documento esistente nell'app collection
                    transaction.set(
                        appuntamentoPath.collection("app").document("$orarioInizio-$orarioFine"),
                        appuntamento
                    )
                } else {
                    // Crea il documento principale e aggiungi il sottodocumento
                    transaction.set(appuntamentoPath, emptyMap<String, Any>())
                    transaction.set(
                        appuntamentoPath.collection("app").document("$orarioInizio-$orarioFine"),
                        appuntamento
                    )
                }

                // Gestisci la collezione occupati
                if (occupatiSnapshot.exists()) {
                    // Verifica se lo slot è già occupato
                    val occupatiMap = occupatiSnapshot.data
                    if (occupatiMap?.containsKey(chiave) == true) {
                        throw FirebaseFirestoreException(
                            "Lo slot $orarioInizio-$orarioFine è già occupato",
                            FirebaseFirestoreException.Code.ABORTED
                        )
                    } else {
                        // Aggiorna lo slot come occupato
                        transaction.update(occupatiPath, chiave, "occupato")
                    }
                } else {
                    // Crea un nuovo documento per la data e segna lo slot come occupato
                    transaction.set(occupatiPath, hashMapOf(chiave to "occupato"))
                }

                if (userSnapshot.exists()){
                    transaction.update(
                        utenteRiferimento,
                        "appuntamenti",
                        FieldValue.arrayUnion(appuntamentoPath.collection("app").document("$orarioInizio-$orarioFine"))
                    )
                }

                onSuccess()
            }.await()

            Log.d("Riuscito", "finito")
            Log.d("Appuntamento", "Prenotazione aggiunta con successo")

        } catch (e: FirebaseFirestoreException) {
            onFailed()
            Log.e("Appuntamento", "Errore durante l'aggiunta dell'appuntamento: ${e.message}", e)
            // Gestisci il conflitto (es. mostra un messaggio all'utente)
            if (e.code == FirebaseFirestoreException.Code.ABORTED) {
                // Lo slot è già stato occupato
                Log.e("Appuntamento", "Lo slot è già occupato!")
            }
        }
    }

    fun updateDati(
        nome : String,
        cognome : String,
        eta : Int,
        telefono : String,
        callback: () -> Unit
    ){

        val daAggiornare = mapOf(
            "nome" to nome,
            "cognome" to cognome,
            "eta" to eta,
            "telefono" to telefono
        )

        auth.currentUser!!.email?.let {
            db.collection("utenti").document(it).update(daAggiornare)
                .addOnSuccessListener {
                    _userState.value = _userState.value.copy(
                        nome = nome,
                        cognome = cognome,
                        eta = eta,
                        telefono = telefono
                    )
                    callback()
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

    fun sincronizzaPrenotazioni(){

        auth.currentUser?.email?.let {
            db.collection("utenti").document(it).addSnapshotListener{ snapshot, error ->

                if (error != null) {
                    Log.e("FirestoreError", "Errore nel ricevere aggiornamenti: ", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val appuntamentiList = snapshot.get("appuntamenti") as? List<DocumentReference>

                    if (appuntamentiList != null) {
                        viewModelScope.launch {
                            var listApp = recuperaDocumenti(appuntamentiList)
                            listApp.forEach{app->
                                app.descrizione = app.descrizione.replace(Regex("\\s+"), " ")
                            }

                            val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                            listApp = listApp.sortedWith(compareByDescending<Appuntamento> {
                                LocalDate.parse(it.data, dateFormatter)
                            }.thenByDescending {
                                LocalTime.parse(it.orarioInizio, timeFormatter)
                            })

                            _listaAppuntamenti.value = listApp
                        }
                    }
                }

            }
        }
    }


    fun annullaPrenotazione(appuntamento: Appuntamento, finito : () -> Unit, errore : () -> Unit){

        val appuntamentoPath = db.collection("appuntamenti").document(appuntamento.data)
        val occupatiPath = db.collection("occupati").document(appuntamento.data)
        val utenteRiferimento: DocumentReference = db.collection("utenti").document(_userState.value.email.toString())


        db.runTransaction {transaction ->
            val appuntamentoSnapshot = transaction.get(appuntamentoPath)
            val occupatiSnapshot = transaction.get(occupatiPath)
            val userSnapshot = transaction.get(utenteRiferimento)
            val chiave = "${appuntamento.orarioInizio}-${appuntamento.orarioFine}"
            val appuntamentoReference = db.collection("appuntamenti").document(appuntamento.data).collection("app").document(chiave)

            if (appuntamentoSnapshot.exists()) {
                transaction.delete(
                    appuntamentoPath.collection("app").document(chiave)
                )
            }

            if (occupatiSnapshot.exists()){
                transaction.update(
                    occupatiPath, chiave, FieldValue.delete()
                )
            }

            if(userSnapshot.exists()){
                transaction.update(
                    utenteRiferimento, "appuntamenti", FieldValue.arrayRemove(appuntamentoReference)
                )
            }

        }.addOnSuccessListener {
            finito()
        }.addOnFailureListener {
            finito()
            errore()
        }.addOnCanceledListener {
            finito()
            errore()
        }


    }



}

