package com.example.marinobarbersalon.Admin

import androidx.lifecycle.ViewModel
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val _adminState = MutableStateFlow(Admin(state = AuthState.None))
    val adminState : StateFlow<Admin> = _adminState.asStateFlow()

    init {
        checkAuthState()
    }

    fun checkAuthState() {
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
            _adminState.value = _adminState.value.copy(
                state = AuthState.Error("Email e password non possono essere vuoti")
            )
            return
        }

        _adminState.value = _adminState.value.copy(state = AuthState.Loading)

        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isAdmin(email) { isAdmin ->
                            if (isAdmin) {
                                _adminState.value = _adminState.value.copy(state = AuthState.Authenticated)
                            } else {
                                _adminState.value = _adminState.value.copy(
                                    state = AuthState.Error("Non hai i privilegi di amministratore")
                                )
                            }
                        }
                    } else {
                        _adminState.value = _adminState.value.copy(
                            state = AuthState.Error(task.exception?.message ?: "Errore generico")
                        )
                    }
                }
        } catch (e: Exception) {
            _adminState.value = _adminState.value.copy(
                state = AuthState.Error("Errore durante il login: ${e.message}")
            )
        }
    }



    fun logout(){
        auth.signOut()
        _adminState.value = _adminState.value.copy(state = AuthState.Unauthenticated)
    }


}