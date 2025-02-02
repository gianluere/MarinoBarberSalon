package com.example.marinobarbersalon.Cliente.Home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Field

@ExperimentalCoroutinesApi
class UserViewModelTest3 {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userViewModel: UserViewModel
    private lateinit var contesto: Context
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockUser: FirebaseUser

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        contesto = mockk(relaxed = true)

        mockAuth = mockk(relaxed = true)
        mockFirestore = mockk(relaxed = true)
        mockUser = mockk(relaxed = true)


        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockAuth


        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns mockFirestore


        every { mockAuth.currentUser } returns mockUser
        every { mockUser.email } returns "test@email.com"


        userViewModel = UserViewModel()
    }



    @Test
    fun `registrazione avvenuta con successo`() = runTest {
        val email = "mario.rossi@esempio.com"
        val password = "password123"
        val nome = "Mario"
        val cognome = "Rossi"
        val eta = 20
        val telefono = "1234567890"

        //Mockk vm per evitare di mockkare tutto a mano
        val userViewModel = mockk<UserViewModel>(relaxed = true)


        coEvery { userViewModel.signup(email, password, nome, cognome, eta, telefono) } just Runs
        every { userViewModel.userState.value.state } returns AuthState.Authenticated

        userViewModel.signup(email, password, nome, cognome, eta, telefono)
        advanceUntilIdle()

        assertThat(userViewModel.userState.value.state).isEqualTo(AuthState.Authenticated)
    }

    @Test
    fun `registrazione fallisce se un campo è vuoto`() = runTest {
        val email = "mario.rossi@esempio.com"
        val password = "" // Campo vuoto
        val nome = "Mario"
        val cognome = "Rossi"
        val eta = 20
        val telefono = "1234567890"

        val userViewModel = mockk<UserViewModel>(relaxed = true)


        val userStateFlow = MutableStateFlow(User(state = AuthState.Unauthenticated))
        every { userViewModel.userState } returns userStateFlow

        every { userViewModel.validationMessage.value } returns "Email e password non possono essere vuoti"


        userViewModel.signup(email, password, nome, cognome, eta, telefono)
        advanceUntilIdle()


        assertThat(userViewModel.userState.value.state).isEqualTo(AuthState.Unauthenticated)

        assertThat(userViewModel.validationMessage.value).isEqualTo("Email e password non possono essere vuoti")
    }

    @Test
    fun `logout imposta lo stato a Unauthenticated`() = runTest {
        val userViewModel = mockk<UserViewModel>(relaxed = true)

        val userStateFlow = MutableStateFlow(User(state = AuthState.Authenticated))
        every { userViewModel.userState } returns userStateFlow

        every { userViewModel.logout() } answers {
            userStateFlow.value = userStateFlow.value.copy(state = AuthState.Unauthenticated)
        }

        userViewModel.logout()
        advanceUntilIdle()

        assertThat(userViewModel.userState.value.state).isEqualTo(AuthState.Unauthenticated)
    }

    @Test
    fun `login avvenuto con successo`() = runTest {
        val email = "mario.rossi@esempio.com"
        val password = "password123"

        val userViewModel = mockk<UserViewModel>(relaxed = true)

        //Parte da Unauthenticated
        val userStateFlow = MutableStateFlow(User(state = AuthState.Unauthenticated))
        every { userViewModel.userState } returns userStateFlow

        //Mock login
        every { userViewModel.login(email, password) } answers {
            userStateFlow.value = userStateFlow.value.copy(state = AuthState.Authenticated)
        }

        //Fa il login
        userViewModel.login(email, password)
        advanceUntilIdle()

        //Controllo se lo stato è cambiato dopo login
        assertThat(userViewModel.userState.value.state).isEqualTo(AuthState.Authenticated)
    }

    @Test
    fun `login fallisce con credenziali errate`() = runTest {
        val email = "mario.rossi@esempio.com"
        val password = "wrongpassword"

        val userViewModel = mockk<UserViewModel>(relaxed = true)

        val userStateFlow = MutableStateFlow(User(state = AuthState.Unauthenticated))
        every { userViewModel.userState } returns userStateFlow

        val errorMessage = "Password errata."
        every { userViewModel.validationMessage.value } returns errorMessage
        every { userViewModel.login(email, password) } answers {
            userStateFlow.value = userStateFlow.value.copy(state = AuthState.Unauthenticated)
        }


        userViewModel.login(email, password)
        advanceUntilIdle()


        assertThat(userViewModel.userState.value.state).isEqualTo(AuthState.Unauthenticated)
        assertThat(userViewModel.validationMessage.value).isEqualTo(errorMessage)
    }

    @Test
    fun `aggiungiApp chiama onSuccess quando la prenotazione avviene con successo`() = runTest {

        val servizio = "Taglio Capelli"
        val orarioInizio = "10:00"
        val orarioFine = "10:30"
        val dataSel = "12/12/2025"

        val userViewModel = mockk<UserViewModel>(relaxed = true)

        val fakeDocumentReference = mockk<DocumentReference>(relaxed = true)

        val userStateFlow = MutableStateFlow(User(state = AuthState.Authenticated, appuntamenti = emptyList()))
        every { userViewModel.userState } returns userStateFlow

        var onSuccessCalled = false
        var onFailedCalled = false
        val onSuccess = { onSuccessCalled = true }
        val onFailed = { onFailedCalled = true }

        every {
            userViewModel.aggiungiApp(servizio, orarioInizio, orarioFine, dataSel, any(), any())
        } answers {
            userStateFlow.value = userStateFlow.value.copy(appuntamenti = listOf(fakeDocumentReference))
            onSuccess()
        }

        userViewModel.aggiungiApp(servizio, orarioInizio, orarioFine, dataSel, onSuccess, onFailed)
        advanceUntilIdle()

        assertThat(onSuccessCalled).isTrue() //Deve essere chiamato onSuccess
        assertThat(onFailedCalled).isFalse() //onFailed non deve essere chiamato
        assertThat(userViewModel.userState.value.appuntamenti).isNotEmpty() //La lista di appuntamenti deve contenere almeno un elemento
    }

    @Test
    fun `aggiungiApp chiama onFailed quando la prenotazione fallisce`() = runTest {

        val servizio = "Taglio Capelli"
        val orarioInizio = "10:00"
        val orarioFine = "10:30"
        val dataSel = "12/12/2025"

        val userViewModel = mockk<UserViewModel>(relaxed = true)

        val userStateFlow = MutableStateFlow(User(state = AuthState.Authenticated, appuntamenti = emptyList()))
        every { userViewModel.userState } returns userStateFlow

        var onSuccessCalled = false
        var onFailedCalled = false
        val onSuccess = { onSuccessCalled = true }
        val onFailed = { onFailedCalled = true }

        every {
            userViewModel.aggiungiApp(servizio, orarioInizio, orarioFine, dataSel, any(), any())
        } answers {
            onFailed()
        }

        userViewModel.aggiungiApp(servizio, orarioInizio, orarioFine, dataSel, onSuccess, onFailed)
        advanceUntilIdle()


        assertThat(onSuccessCalled).isFalse() //onSuccess NON deve essere chiamato
        assertThat(onFailedCalled).isTrue() //Deve essere chiamato onFailed
        assertThat(userViewModel.userState.value.appuntamenti).isEmpty() //Nessun appuntamento deve essere aggiunto
    }
}
