import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Home.User
import com.example.marinobarbersalon.Cliente.Shopping.Prodotto
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoPrenotato
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProdottiPrenotatiVM : ViewModel() {


    private val firestore = FirebaseFirestore.getInstance()

    private val _listaProdottiPrenotati = MutableStateFlow<List<Pair<ProdottoPrenotato, Prodotto>>>(emptyList())
    val listaProdottiPrenotati: StateFlow<List<Pair<ProdottoPrenotato, Prodotto>>> = _listaProdottiPrenotati.asStateFlow()

    private val _isLoadingProdotti = MutableStateFlow(false)
    val isLoadingProdotti: StateFlow<Boolean> = _isLoadingProdotti.asStateFlow()


    fun fetchProdottiPrenotatiAttesa() {
        viewModelScope.launch {
            _isLoadingProdotti.value = true
            try {
                val prodottiPrenotati = getProdottiPrenotatiFromFirestore()
                _listaProdottiPrenotati.value = prodottiPrenotati
            } catch (e: Exception) {
                Log.e("ProdottiPrenotatiVM", e.toString())
            } finally {
                _isLoadingProdotti.value = false
            }
        }
    }

    private suspend fun getProdottiPrenotatiFromFirestore(): List<Pair<ProdottoPrenotato, Prodotto>> {
        val prodottiPrenotati = mutableListOf<Pair<ProdottoPrenotato, Prodotto>>()

        val snapshot = firestore.collection("prodottiPrenotati")
            .whereEqualTo("stato", "attesa")
            .get()
            .await()

        for (document in snapshot.documents) {
            val prodottoPrenotato = document.toObject(ProdottoPrenotato::class.java)
            val prodottoSnapshot = prodottoPrenotato?.prodotto?.get()?.await()
            val prodotto = prodottoSnapshot?.toObject(Prodotto::class.java)

            if (prodottoPrenotato != null && prodotto != null) {
                prodottiPrenotati.add(prodottoPrenotato to prodotto)
            }
        }

        return prodottiPrenotati
    }

    //Cambia stato a "prodotto prenotato" da "attesa" a "confermato"
    fun confermaProdotto(prodottoPrenotato: ProdottoPrenotato) {
        viewModelScope.launch {
            try {
                val querySnapshot = firestore.collection("prodottiPrenotati")
                    .whereEqualTo("prodotto", prodottoPrenotato.prodotto)
                    .whereEqualTo("quantita", prodottoPrenotato.quantita)
                    .whereEqualTo("utente", prodottoPrenotato.utente)
                    .whereEqualTo("data", prodottoPrenotato.data)
                    .whereEqualTo("stato", prodottoPrenotato.stato)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    firestore.collection("prodottiPrenotati")
                        .document(document.id)
                        .update("stato", "confermato")
                        .await()
                }
//                Log.d("ProdottiPrenotatiVM", "Prodotto confermato: ${prodottoPrenotato.prodotto}")

                //Tolgo dalla lista presa prima
                _listaProdottiPrenotati.value = _listaProdottiPrenotati.value.filterNot { it.first == prodottoPrenotato }

            } catch (e: Exception) {
                Log.e("ProdottiPrenotatiVM", "Errore durante la conferma del prodotto: ${e.message}")
            }
        }
    }

    //Per prendere i dettagli del cliente che ha prenotato il prodotto
    fun fetchClienteDetails(
        utenteReference: DocumentReference,
        onSuccess: (String, String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val utenteSnapshot = utenteReference.get().await()
                val utente = utenteSnapshot.toObject(User::class.java)

                if (utente != null) {
                    val nome = utente.nome ?: "Nome non disponibile"
                    val cognome = utente.cognome ?: "Cognome non disponibile"
//                    Log.d("ProdottiPrenotatiVM", "Nome: $nome, Cognome: $cognome")
                    onSuccess(nome, cognome)
                } else {
                    onError(Exception("Utente non trovato"))
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }






}
