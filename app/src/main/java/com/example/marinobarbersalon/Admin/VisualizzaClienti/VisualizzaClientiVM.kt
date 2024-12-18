package com.example.marinobarbersalon.Admin.VisualizzaClienti

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Home.UserFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


//model assente in quanto utilizzo la classe UserFirebase
class VisualizzaClientiVM : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _usersState = MutableStateFlow<List<UserFirebase>>(emptyList())
    val usersState: StateFlow<List<UserFirebase>> = _usersState.asStateFlow()

    //Stato per singolo cliente
    private val _selectedClienteState = MutableStateFlow<UserFirebase?>(null)
    val selectedClienteState: StateFlow<UserFirebase?> = _selectedClienteState.asStateFlow()

    //PER LA RICERCA
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _clientiState = MutableStateFlow<List<UserFirebase>>(emptyList())
    val clientiState = _clientiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingCliente = MutableStateFlow(false)
    val isLoadingCliente: StateFlow<Boolean> = _isLoadingCliente.asStateFlow()

//    val filteredClients = searchText
//        .debounce(1000L)
//        .combine(_clientiState) { text, clienti ->
//            if (text.isBlank()) {
//                clienti
//            } else {
//                delay(1000L)
//                clienti.filter {
//                    it.doesMatchSearchQuery(text)
//                }
//            }
//        }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _clientiState.value)

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    fun fetchUsers() {
        viewModelScope.launch {
            _isLoading.value = true // Inizia il caricamento
            try {
                val users = getUsersFromFirestore()
                _usersState.value = users
            } catch (e: Exception) {
                Log.e("VisualizzaClientiVM", "Errore nel recupero degli utenti", e)
                _usersState.value = emptyList()
            } finally {
                _isLoading.value = false // Termina il caricamento
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
            _isLoadingCliente.value = true // Inizia il caricamento
            try {
                val cliente = getUserByEmailFromFirestore(clienteEmail)
                _selectedClienteState.value = cliente
            } catch (e: Exception) {
                Log.e("DettagliCliente", "Errore nel recupero del cliente $clienteEmail", e)
                _selectedClienteState.value = null
            } finally {
                _isLoadingCliente.value = false // Termina il caricamento
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
