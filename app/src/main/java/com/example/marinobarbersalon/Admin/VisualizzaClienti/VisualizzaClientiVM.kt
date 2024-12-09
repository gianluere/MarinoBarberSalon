package com.example.marinobarbersalon.Admin.VisualizzaClienti

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Home.UserFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


//model assente in quanto utilizzo la classe UserFirebase
class VisualizzaClientiVM : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // Stato per gli utenti
    private val _usersState = MutableStateFlow<List<UserFirebase>>(emptyList())
    val usersState: StateFlow<List<UserFirebase>> = _usersState.asStateFlow()

    // Stato per un singolo cliente
    private val _selectedClienteState = MutableStateFlow<UserFirebase?>(null)
    val selectedClienteState: StateFlow<UserFirebase?> = _selectedClienteState.asStateFlow()


    // Recupera gli utenti da Firestore
    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val users = getUsersFromFirestore()
                _usersState.value = users
            } catch (e: Exception) {
                Log.e("VisualizzaClientiVM", "Errore nel recupero degli utenti", e)
                _usersState.value = emptyList()
            }
        }
    }

    private suspend fun getUsersFromFirestore(): List<UserFirebase> {
        val db = firestore.collection("utenti")
        val snapshot = db.get().await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(UserFirebase::class.java)
        }
    }

    fun getClienteByEmail(clienteEmail: String) {
        viewModelScope.launch {
            try {
                val cliente = getUserByEmailFromFirestore(clienteEmail)
                _selectedClienteState.value = cliente
            } catch (e: Exception) {
                Log.e("HI", "Errore  recupero cliente $clienteEmail", e)
                _selectedClienteState.value = null
            }
        }
    }

    private suspend fun getUserByEmailFromFirestore(clienteEmail: String): UserFirebase? {
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("utenti").document(clienteEmail)
        val documentSnapshot = documentRef.get().await()

        return if (documentSnapshot.exists()) {
            documentSnapshot.toObject(UserFirebase::class.java)
        } else {
            Log.d("HI", "Documento non trovato per l email: $clienteEmail")
            null
        }
    }









}
