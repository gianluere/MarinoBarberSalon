package com.example.marinobarbersalon.Cliente.Home

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.example.marinobarbersalon.Cliente.Shopping.Prodotto
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoPrenotato
import com.example.marinobarbersalon.NotificationWorker
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.marinobarbersalon.LoginRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class UserViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow(User(state = AuthState.None))
    val userState : StateFlow<User> = _userState.asStateFlow()
    private val db = Firebase.firestore

    private val _listaAppuntamenti = MutableStateFlow(listOf<Appuntamento>())
    val listaAppuntamenti : StateFlow<List<Appuntamento>> = _listaAppuntamenti.asStateFlow()

    private val _listaProdottiPrenotati = MutableStateFlow<List<Pair<ProdottoPrenotato, Prodotto>>>(emptyList())
    val listaProdottiPrenotati : StateFlow<List<Pair<ProdottoPrenotato, Prodotto>>> = _listaProdottiPrenotati.asStateFlow()

    //per login
    private val _validationMessage = MutableStateFlow<String?>(null)
    val validationMessage: StateFlow<String?> = _validationMessage.asStateFlow()

    private var loginRepository: LoginRepository = LoginRepository()

    //errori possibili da firebase
    private val firebaseErrorMessages = mapOf(
        "ERROR_INVALID_EMAIL" to "L'indirizzo email fornito non è valido. Controlla e riprova.",
        "ERROR_INVALID_CREDENTIAL" to "Le credenziali fornite sono errate, non valide o scadute.",
        "ERROR_USER_NOT_FOUND" to "Utente non trovato.",
        "ERROR_WRONG_PASSWORD" to "Password errata.",
        "ERROR_EMAIL_ALREADY_IN_USE" to "L'email è già in uso. Usa un'email diversa.",
        "ERROR_WEAK_PASSWORD" to "La password è troppo debole. Scegli una password più sicura."
    )

    init {
        checkAuthState()

    }

    fun setLoginRepositoryForTest(repository: LoginRepository) {
        this.loginRepository = repository
    }

    fun checkAuthState() {

        _userState.value = _userState.value.copy(state = AuthState.Loading)

        val currentUser = auth.currentUser
        if (currentUser == null) {
            _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)
            Log.d("LOGIN", "User non trovato e vuoto")
        } else {
            checkIfCliente(currentUser.email ?: "") { isCliente ->
                if (isCliente) {
                    Log.d("LOGIN", "User autenticato e non vuoto")
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                    caricaDati()
                    sincronizzaPrenotazioni()
                    Log.d("LOGIN", "User autenticato e non vuoto 2")
                } else {
                    _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)
                    Log.d("LOGIN", "User autenticato e non vuoto 3")
                }
            }
        }
    }

    //uso questa funzione per vedere se esiste il documento su firebase relativo a questa email
    private fun checkIfCliente(email: String, callback: (Boolean) -> Unit) {
        // Funzione per verificare se l'email è associata a un amministratore
        if (email.isBlank()) {
            callback(false)
            return
        }

        db.collection("utenti").document(email).get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener {
                Log.e("UserViewModel", "Errore nel verificare se è cliente: ${it.message}")
                callback(false)
            }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            //Altrimenti mi fa vedere questo errore solo una volta
            _validationMessage.value = null
            _validationMessage.value = "Email e password non possono essere vuoti"
            return
        }

        _validationMessage.value = null // Resetta il messaggio di errore
        _userState.value = _userState.value.copy(state = AuthState.Loading)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                } else {
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    Log.d("ToaError", errorCode)
                    val errorMessage = firebaseErrorMessages[errorCode] ?: "Si è verificato un errore sconosciuto."
                    _validationMessage.value = errorMessage
                    //_userState.value = _userState.value.copy(state = AuthState.Error(errorMessage))
                    _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)
                }
            }
    }




    fun resetValidationMessage() {
        _validationMessage.value = null
    }



    fun signup(email : String, password : String, nome: String, cognome : String, eta: Int = 0, telefono : String){

        if (email.isBlank() || password.isBlank()){

            _validationMessage.value = null
            _validationMessage.value = "Email e password non possono essere vuoti"

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
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    Log.d("ToaError", errorCode)
                    val errorMessage = firebaseErrorMessages[errorCode] ?: "Si è verificato un errore sconosciuto."
                    _validationMessage.value = errorMessage
                    //_userState.value = _userState.value.copy(state = AuthState.Error(errorMessage))
                    _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)
                }
            }
    }

    fun logout(){
        auth.signOut()
        _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)

    }

    private fun caricaDati(){

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

    //funzione per inviare la notifica dell'appuntamento
    fun scheduleNotificationWithWorker(
        context: Context,
        titolo: String,
        messaggio: String,
        delayInMillis: Long
    ) {
        val data = Data.Builder()
            .putString("title", titolo)
            .putString("message", messaggio)
            .build()

        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(notificationWorkRequest)
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

     suspend fun aggiungiAppuntamento(
        servizio: String,
        orarioInizio: String,
        orarioFine: String,
        dataSel: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        val data = dataSel.replace("/", "-")
        Log.d("Appuntamento", servizio)

        val results = db.collection("servizi").whereEqualTo("nome", servizio).limit(1).get().await()

        val servizioNome: String = results.documents[0].get("nome").toString()
        val descrizione: String = results.documents[0].get("descrizione").toString()
        val prezzo: Double = results.documents[0].get("prezzo").toString().toDouble()

        val utenteRiferimento: DocumentReference = db.collection("utenti").document(_userState.value.email.toString())

        val appuntamento = hashMapOf(
            "cliente" to utenteRiferimento,
            "orarioInizio" to orarioInizio,
            "orarioFine" to orarioFine,
            "data" to data,
            "servizio" to servizioNome,
            "descrizione" to descrizione,
            "prezzo" to prezzo
        )

        val appuntamentoPath = db.collection("appuntamenti").document(data)
        val occupatiPath = db.collection("occupati").document(data)
        val totalePath = appuntamentoPath.collection("totale").document("count")

        try {
            //Transazione per garantire che tutte le operazioni vengano svolte
            db.runTransaction { transaction ->
                val appuntamentoSnapshot = transaction.get(appuntamentoPath)
                val occupatiSnapshot = transaction.get(occupatiPath)
                val totaleSnapshot = transaction.get(totalePath)
                val chiave = "$orarioInizio-$orarioFine"

                // Aggiungi appuntamento
                if (appuntamentoSnapshot.exists()) {

                    transaction.set(
                        appuntamentoPath.collection("app").document(chiave),
                        appuntamento
                    )
                } else {
                    //il primo vuoto mi serve solo per creare il documento
                    transaction.set(appuntamentoPath, emptyMap<String, Any>())
                    transaction.set(
                        appuntamentoPath.collection("app").document(chiave),
                        appuntamento
                    )
                }

                //Aggiorno la collezione "totale" incrementando il conteggio
                if (totaleSnapshot.exists()) {
                    val currentCount = totaleSnapshot.getLong("count") ?: 0
                    transaction.update(totalePath, "count", currentCount + 1)
                } else {
                    transaction.set(totalePath, mapOf("count" to 1))
                }

                //Aggiorno la collection occupati
                if (occupatiSnapshot.exists()) {
                    val occupatiMap = occupatiSnapshot.data
                    if (occupatiMap?.containsKey(chiave) == true) {
                        throw FirebaseFirestoreException(
                            "Lo slot $orarioInizio-$orarioFine è già occupato",
                            FirebaseFirestoreException.Code.ABORTED
                        )
                    } else {
                        transaction.update(occupatiPath, chiave, "occupato")
                    }
                } else {
                    transaction.set(occupatiPath, hashMapOf(chiave to "occupato"))
                }

                //Aggiorna la lista appuntamenti dell'utente
                transaction.update(
                    utenteRiferimento,
                    "appuntamenti",
                    FieldValue.arrayUnion(appuntamentoPath.collection("app").document(chiave))
                )
            }.await()

            onSuccess()
            Log.d("Appuntamento", "Prenotazione aggiunta con successo e totale aggiornato")
        } catch (e: FirebaseFirestoreException) {
            onFailed()
            Log.e("Appuntamento", "Errore durante l'aggiunta dell'appuntamento: ${e.message}", e)
        }
    }

    //funzione per aggiornare i propri dati personali
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


    //funzione di appoggio che mi converte i docRef in oggetti Appuntamento
    private suspend fun recuperaDocumenti(listaAppuntamenti : List<DocumentReference>): List<Appuntamento>{
        val appuntamenti = mutableListOf<Appuntamento>()

        for (document in listaAppuntamenti){
            val result = document.get().await()

            result.toObject(Appuntamento::class.java)?.let { appuntamenti.add(it) }
        }

        return appuntamenti
    }

    //listener automatico per l'aggiornamento delle prenotazioni
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
                                //c'è un bug per il doppio spazio che si vede come un a capo
                                app.descrizione = app.descrizione.replace(Regex("\\s+"), " ")
                            }

                            val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                            //riordino la lista
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


    fun annullaPrenotazione(appuntamento: Appuntamento, finito: () -> Unit, errore: () -> Unit) {

        //Prendo i riferimenti ai vari documenti

        val appuntamentoPath = db.collection("appuntamenti").document(appuntamento.data)
        val occupatiPath = db.collection("occupati").document(appuntamento.data)
        val utenteRiferimento: DocumentReference = db.collection("utenti").document(_userState.value.email.toString())
        val totalePath = appuntamentoPath.collection("totale").document("count")

        //Transazione per effettuare tutte le operazioni
        db.runTransaction { transaction ->
            val appuntamentoSnapshot = transaction.get(appuntamentoPath)
            val occupatiSnapshot = transaction.get(occupatiPath)
            val totaleSnapshot = transaction.get(totalePath)
            val userSnapshot = transaction.get(utenteRiferimento)

            val chiave = "${appuntamento.orarioInizio}-${appuntamento.orarioFine}"
            val appuntamentoReference =
                db.collection("appuntamenti").document(appuntamento.data).collection("app").document(chiave)

            //Cancella l'appuntamento
            if (appuntamentoSnapshot.exists()) {
                transaction.delete(appuntamentoPath.collection("app").document(chiave))
            }

            //Aggiorna il documento "occupati" rimuovendo la chiave
            if (occupatiSnapshot.exists()) {
                transaction.update(occupatiPath, chiave, FieldValue.delete())
            }

            //Rimuove il riferimento appuntamento dall'utente
            if (userSnapshot.exists()) {
                transaction.update(
                    utenteRiferimento,
                    "appuntamenti",
                    FieldValue.arrayRemove(appuntamentoReference)
                )
            }

            //Aggiorna il conteggio nella collezione "totale"
            if (totaleSnapshot.exists()) {
                val currentCount = totaleSnapshot.getLong("count") ?: 0
                if (currentCount > 0) {
                    transaction.update(totalePath, "count", currentCount - 1)
                } else {
                    transaction.update(totalePath, "count", 0) //Garantisce che il conteggio non vada sotto zero
                }
            } else {
                //Se per qualche motivo il documento totale non esiste, lo crea con valore 0
                transaction.set(totalePath, mapOf("count" to 0))
            }

        }.addOnSuccessListener {
            finito()
            Log.d("AnnullaPrenotazione", "Prenotazione annullata con successo e totale aggiornato")
        }.addOnFailureListener {
            errore()
            Log.e("AnnullaPrenotazione", "Errore durante l'annullamento della prenotazione")
        }.addOnCanceledListener {
            errore()
            Log.e("AnnullaPrenotazione", "Annullamento della prenotazione annullato")
        }
    }


    //Carica la lista dei prodotti prenotati che sono in stato di attesa
    fun caricaProdottiPrenotati(){

        val listaProdPren = mutableListOf<ProdottoPrenotato>()
        val listaProdottiAssociati = mutableListOf<Pair<ProdottoPrenotato, Prodotto>>()

        val userReference = auth.currentUser?.email?.let { db.collection("utenti").document(it) }

        // Recupera i prodotti prenotati
        db.collection("prodottiPrenotati")
            .whereEqualTo("utente", userReference)
            .whereEqualTo("stato", "attesa")
            .get()
            .addOnSuccessListener { result ->
                val taskList = mutableListOf<Task<DocumentSnapshot>>() //Lista per gestire le richieste asincrone

                //Aggiungo i prodotti prenotati alla lista
                for (document in result) {
                    val prodottoPrenotato = document.toObject<ProdottoPrenotato>()

                    listaProdPren.add(prodottoPrenotato)

                    //Recupero il prodotto associato poichè è un docRef
                    val prodottoTask = prodottoPrenotato.prodotto?.get()
                    if (prodottoTask != null) {
                        taskList.add(prodottoTask) //Aggiungo il task alla lista per attendere il completamento
                    }

                    // Quando il task di recupero del prodotto è completato
                    prodottoTask?.addOnSuccessListener { prodottoDoc ->
                        val prodotto = prodottoDoc.toObject<Prodotto>()
                        if (prodotto != null) {
                            Log.d("Prenotato", prodotto.nome)
                            listaProdottiAssociati.add(Pair(prodottoPrenotato, prodotto))
                        }
                    }
                }

                //Attende che tutte le richieste per i prodotti siano completate prima di aggiornare la lista
                Tasks.whenAllComplete(taskList).addOnCompleteListener {
                    listaProdottiAssociati.sortBy { it.second.nome }
                    _listaProdottiPrenotati.value = listaProdottiAssociati
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Errore nel caricamento dei prodotti prenotati", exception)
            }



    }

    fun annullaPrenotazioneProdotto(prodotto : ProdottoPrenotato, onFinish : () -> Unit){

        db.collection("prodottiPrenotati")
            .whereEqualTo("prodotto", prodotto.prodotto)
            .whereEqualTo("quantita", prodotto.quantita)
            .whereEqualTo("utente", prodotto.utente)
            .whereEqualTo("data", prodotto.data)
            .whereEqualTo("stato", "attesa").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    for (document in result) {
                        document.reference.delete()
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { e ->
                                println("Errore durante la cancellazione: ${e.message}")
                            }
                    }
                }

                prodotto.prodotto?.let { prodottoReference ->
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(prodottoReference)

                        if (snapshot.exists()) {
                            val quantitaAttuale = snapshot.getLong("quantita") ?: 0L
                            val nuovaQuantita = quantitaAttuale + prodotto.quantita


                            transaction.update(prodottoReference, "quantita", nuovaQuantita)
                        }
                    }.addOnSuccessListener {
                        caricaProdottiPrenotati()
                        println("Transazione completata con successo.")
                    }.addOnFailureListener { e ->
                        println("Errore durante la transazione: ${e.message}")
                    }
                }

                onFinish()
            }
            .addOnFailureListener { e ->
                println("Errore durante il recupero dei documenti: ${e.message}")
            }
    }



}

