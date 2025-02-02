package com.example.marinobarbersalon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marinobarbersalon.Cliente.Account.ListaRecensioniViewModel
import com.example.marinobarbersalon.Cliente.Account.Recensione
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListaRecensioniVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var listaRecensioniVM: ListaRecensioniViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        listaRecensioniVM = mockk(relaxed = true)
    }

    @Test
    fun `inserisciRecensione chiama onCompleted quando la recensione viene aggiunta con successo`() = runTest {

        val recensione = Recensione(
            nome = "Mario Rossi",
            stelle = 4.5,
            descrizione = "Servizio eccellente!"
        )


        var onCompletedCalled = false
        val onCompleted = { onCompletedCalled = true }

        every { listaRecensioniVM.inserisciRecensione(recensione, any()) } answers {
            onCompleted()
        }

        listaRecensioniVM.inserisciRecensione(recensione, onCompleted)
        advanceUntilIdle()

        assertThat(onCompletedCalled).isTrue()
    }
}
