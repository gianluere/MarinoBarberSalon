import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Cliente.Shopping.Prodotto
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoPrenotato
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProdottiPrenotatiVM : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // Lista di prodotti prenotati con dettagli
    private val _listaProdottiPrenotati = MutableStateFlow<List<Pair<ProdottoPrenotato, Prodotto>>>(emptyList())
    val listaProdottiPrenotati: StateFlow<List<Pair<ProdottoPrenotato, Prodotto>>> = _listaProdottiPrenotati.asStateFlow()

    private val _isLoadingProdotti = MutableStateFlow(false)
    val isLoadingProdotti: StateFlow<Boolean> = _isLoadingProdotti.asStateFlow()

    // Carica i prodotti prenotati con stato "attesa"
    fun fetchProdottiPrenotatiAttesa() {
        viewModelScope.launch {
            _isLoadingProdotti.value = true // Inizia il caricamento
            try {
                val prodottiPrenotati = getProdottiPrenotatiFromFirestore()
                _listaProdottiPrenotati.value = prodottiPrenotati
            } catch (e: Exception) {
                Log.e("ProdottiPrenotatiVM", e.toString())
            } finally {
                _isLoadingProdotti.value = false // Termina il caricamento
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

    // Conferma un prodotto aggiornando il suo stato a "confermato"
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

                // Rimuovi il prodotto dalla lista locale
                _listaProdottiPrenotati.value = _listaProdottiPrenotati.value.filterNot { it.first == prodottoPrenotato }

            } catch (e: Exception) {
                Log.e("ProdottiPrenotatiVM", "Errore durante la conferma del prodotto: ${e.message}")
            }
        }
    }
}
