package com.example.marinobarbersalon.Admin

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val _adminState = MutableStateFlow(Admin(state = AuthState.None))
    val adminState : StateFlow<Admin> = _adminState.asStateFlow()

    //per login
    private val _validationMessage = MutableStateFlow<String?>(null)
    val validationMessage: StateFlow<String?> = _validationMessage.asStateFlow()

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

    fun checkAuthState() {
        _adminState.value = _adminState.value.copy(state = AuthState.Loading)

        val currentUser = auth.currentUser
        if (currentUser == null) {
            _adminState.value = _adminState.value.copy(state = AuthState.Unauthenticated)
        } else {
            val email = currentUser.email
            if (email.isNullOrBlank()) {
                _adminState.value = _adminState.value.copy(state = AuthState.Unauthenticated)
                return
            }

            isAdmin(email) { isAdmin ->
                if (isAdmin) {
                    _adminState.value = _adminState.value.copy(state = AuthState.Authenticated)
                } else {
                    _adminState.value = _adminState.value.copy(state = AuthState.Unauthenticated)
                }
            }
        }
    }


    fun isAdmin(email: String, callback: (Boolean) -> Unit) {
        if (email.isBlank()) {
            callback(false)
            return
        }

        db.collection("admin").document(email).get()
            .addOnSuccessListener { result ->
                // Chiama il callback con il risultato
                callback(result.exists())
            }
            .addOnFailureListener {
                // In caso di errore, chiama il callback con false
                callback(false)
            }
    }


    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            //Perche senno mi fa vedere questo errore solo una volta
            _validationMessage.value = null
            _validationMessage.value = "Email e password non possono essere vuoti"
            return
        }

        _validationMessage.value = null // Resetta il messaggio di errore
        _adminState.value = _adminState.value.copy(state = AuthState.Loading)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _adminState.value = _adminState.value.copy(state = AuthState.Authenticated)
                } else {
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    Log.d("ToaError", errorCode)
                    val errorMessage = firebaseErrorMessages[errorCode] ?: "Si è verificato un errore sconosciuto."
                    _validationMessage.value = errorMessage
                    _adminState.value = _adminState.value.copy(state = AuthState.Error(errorMessage))
                }
            }
    }



    fun resetValidationMessage() {
        _validationMessage.value = null
    }


    fun logout(){
        auth.signOut()
        _adminState.value = _adminState.value.copy(state = AuthState.Unauthenticated)
    }


}