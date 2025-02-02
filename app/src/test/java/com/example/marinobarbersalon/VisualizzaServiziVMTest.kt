package com.example.marinobarbersalon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marinobarbersalon.Admin.Servizi.VisualizzaServiziVM
import com.example.marinobarbersalon.Cliente.Home.Servizio
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VisualizzaServiziVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var firestoreMock: FirebaseFirestore
    private lateinit var viewModelServizi: VisualizzaServiziVM

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        //Mock Firestore
        firestoreMock = mockk(relaxed = true)

        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns firestoreMock

        //Mock della collezione "servizi"
        val collectionMock = mockk<com.google.firebase.firestore.CollectionReference>(relaxed = true)
        every { firestoreMock.collection("servizi") } returns collectionMock

        //Mock dei log altrimenti fallisce il test
        mockkStatic(android.util.Log::class)
        every { android.util.Log.d(any(), any()) } returns 0

        //Creazione VM
        viewModelServizi = VisualizzaServiziVM()
    }



    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `validateForm fallisce con nome vuoto`() = runTest {
        viewModelServizi.onNomeChange("")
        assertThat(viewModelServizi.validateForm()).isFalse()
        assertThat(viewModelServizi.formErrors.value).contains("Il nome non può essere vuoto.")
    }

    @Test
    fun `validateForm fallisce con descrizione vuota`() = runTest {
        viewModelServizi.onDescrizioneChange("")
        assertThat(viewModelServizi.validateForm()).isFalse()
        assertThat(viewModelServizi.formErrors.value).contains("La descrizione non può essere vuota.")
    }

    @Test
    fun `validateForm fallisce con durata zero`() = runTest {
        viewModelServizi.onDurataChange(0)
        assertThat(viewModelServizi.validateForm()).isFalse()
        assertThat(viewModelServizi.formErrors.value).contains("La durata deve essere maggiore di zero.")
    }

    @Test
    fun `validateForm fallisce con prezzo zero`() = runTest {
        viewModelServizi.onPrezzoChange(0.0)
        assertThat(viewModelServizi.validateForm()).isFalse()
        assertThat(viewModelServizi.formErrors.value).contains("Il prezzo deve essere maggiore di zero.")
    }

    @Test
    fun `validateForm passa con dati validi`() = runTest {
        viewModelServizi.onNomeChange("Taglio Uomo")
        viewModelServizi.onDescrizioneChange("Taglio Classico")
        viewModelServizi.onDurataChange(30)
        viewModelServizi.onPrezzoChange(15.0)

        assertThat(viewModelServizi.validateForm()).isTrue()
        assertThat(viewModelServizi.formErrors.value).isEmpty()
    }

    @Test
    fun `aggiunta servizio con form non valido non chiama Firestore`() = runTest {
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onError = mockk<(Exception) -> Unit>(relaxed = true)

        viewModelServizi.onNomeChange("")
        viewModelServizi.aggiungiServizio(onSuccess, onError)

        verify(exactly = 0) { firestoreMock.collection("servizi").add(any()) }
        verify(exactly = 0) { onSuccess() }
        verify(exactly = 0) { onError(any()) }
    }

    @Test
    fun `eliminazione servizio rimuove dalla lista`() = runTest {
        val servizio = Servizio("1", "Taglio Uomo", "Taglio capelli", "Capelli", 30, 20.0)

        viewModelServizi.fetchServizi()
        advanceUntilIdle()

        viewModelServizi.deleteServizio(servizio)
        advanceUntilIdle()

        assertThat(viewModelServizi.serviziState.value).doesNotContain(servizio)
    }

    @Test
    fun `resetFields azzera i campi`() = runTest {
        viewModelServizi.onNomeChange("Taglio Uomo")
        viewModelServizi.onDescrizioneChange("Taglio e piega")
        viewModelServizi.onDurataChange(30)
        viewModelServizi.onPrezzoChange(20.0)

        viewModelServizi.resetFields()

        assertThat(viewModelServizi.nome.value).isEmpty()
        assertThat(viewModelServizi.descrizione.value).isEmpty()
        assertThat(viewModelServizi.durata.value).isEqualTo(0)
        assertThat(viewModelServizi.prezzo.value).isEqualTo(0.00)
    }
}
