package com.example.marinobarbersalon

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.marinobarbersalon.Admin.Servizi.AggiungiServizio
import com.example.marinobarbersalon.Admin.Servizi.VisualizzaServiziVM
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VisualizzaServiziScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: VisualizzaServiziVM

    @Before
    fun setUp() {
        viewModel = VisualizzaServiziVM()
    }

    @Test
    fun validateFormNomeVuoto() {
        // Mostra la UI
        composeTestRule.setContent {
            AggiungiServizio(
                aggiungiServizioViewModel = viewModel,
                onAggiungiSuccess = {},
                onAnnullaClick = {}
            )
        }

        // Simula il click sul pulsante "Aggiungi"
        composeTestRule.onNodeWithText("Aggiungi").performClick()

        // Verifica che il messaggio di errore sia visibile nella UI
        composeTestRule.onNodeWithText("Il nome non può essere vuoto.")
            .assertIsDisplayed()
    }

    @Test
    fun validateFormDescrizioneVuota() {
        composeTestRule.setContent {
            AggiungiServizio(
                aggiungiServizioViewModel = viewModel,
                onAggiungiSuccess = {},
                onAnnullaClick = {}
            )
        }

        // Simula il click sul pulsante "Aggiungi"
        composeTestRule.onNodeWithText("Aggiungi").performClick()

        // Verifica che il messaggio di errore per la descrizione sia visibile nella UI
        composeTestRule.onNodeWithText("La descrizione non può essere vuota.")
            .assertIsDisplayed()
    }

    @Test
    fun validateFormDurataZero() {
        composeTestRule.setContent {
            AggiungiServizio(
                aggiungiServizioViewModel = viewModel,
                onAggiungiSuccess = {},
                onAnnullaClick = {}
            )
        }

        // Aspetta che la UI sia pronta
        composeTestRule.waitForIdle()

        // Controlla che il nodo esista prima di interagire
        composeTestRule.onNodeWithText("Durata (minuti)")
            .assertExists("Durata (minuti) non trovato nella UI")

        // Cancella l'input e inserisce il valore 0
        composeTestRule.onNodeWithText("Durata (minuti)").performTextClearance()
        composeTestRule.onNodeWithText("Durata (minuti)").performTextInput("0")

        // Simula il click sul pulsante "Aggiungi"
        composeTestRule.onNodeWithText("Aggiungi").performClick()

        // Verifica che il messaggio di errore sia visibile nella UI
        composeTestRule.onNodeWithText("La durata deve essere maggiore di zero.")
            .assertIsDisplayed()
    }

    @Test
    fun validateFormPrezzoZero() {
        composeTestRule.setContent {
            AggiungiServizio(
                aggiungiServizioViewModel = viewModel,
                onAggiungiSuccess = {},
                onAnnullaClick = {}
            )
        }

        // Aspetta che la UI sia pronta
        composeTestRule.waitForIdle()

        // Controlla che il nodo esista prima di interagire
        composeTestRule.onNodeWithText("Prezzo (€)")
            .assertExists("Prezzo (€) non trovato nella UI")

        // Cancella l'input e inserisce il valore 0
        composeTestRule.onNodeWithText("Prezzo (€)").performTextClearance()
        composeTestRule.onNodeWithText("Prezzo (€)").performTextInput("0")

        // Simula il click sul pulsante "Aggiungi"
        composeTestRule.onNodeWithText("Aggiungi").performClick()

        // Verifica che il messaggio di errore sia visibile nella UI
        composeTestRule.onNodeWithText("Il prezzo deve essere maggiore di zero.")
            .assertIsDisplayed()
    }

    @Test
    fun validateFormPassaConDatiValidi() {
        composeTestRule.setContent {
            AggiungiServizio(
                aggiungiServizioViewModel = viewModel,
                onAggiungiSuccess = {},
                onAnnullaClick = {}
            )
        }

        // Aspetta che la UI sia pronta
        composeTestRule.waitForIdle()

        // Inserisci i dati validi nei campi
        composeTestRule.onNodeWithText("Nome").performTextClearance()
        composeTestRule.onNodeWithText("Nome").performTextInput("Taglio Uomo")

        composeTestRule.onNodeWithText("Descrizione").performTextClearance()
        composeTestRule.onNodeWithText("Descrizione").performTextInput("Taglio Classico")

        composeTestRule.onNodeWithText("Durata (minuti)").performTextClearance()
        composeTestRule.onNodeWithText("Durata (minuti)").performTextInput("30")

        composeTestRule.onNodeWithText("Prezzo (€)").performTextClearance()
        composeTestRule.onNodeWithText("Prezzo (€)").performTextInput("15.0")

        // Simula il click sul pulsante "Aggiungi"
        composeTestRule.onNodeWithText("Aggiungi").performClick()

        // Verifica che nessun errore sia mostrato
        composeTestRule.onNodeWithText("Il nome non può essere vuoto.").assertDoesNotExist()
        composeTestRule.onNodeWithText("La descrizione non può essere vuota.").assertDoesNotExist()
        composeTestRule.onNodeWithText("La durata deve essere maggiore di zero.").assertDoesNotExist()
        composeTestRule.onNodeWithText("Il prezzo deve essere maggiore di zero.").assertDoesNotExist()
    }

    @Test
    fun resetFieldsAzzeraICampi(){
        composeTestRule.setContent {
            AggiungiServizio(
                aggiungiServizioViewModel = viewModel,
                onAggiungiSuccess = {},
                onAnnullaClick = {}
            )
        }

        // Aspetta che la UI sia pronta
        composeTestRule.waitForIdle()

        // Inserisci i dati validi nei campi
        composeTestRule.onNodeWithText("Nome").performTextClearance()
        composeTestRule.onNodeWithText("Nome").performTextInput("Taglio Uomo")

        composeTestRule.onNodeWithText("Descrizione").performTextClearance()
        composeTestRule.onNodeWithText("Descrizione").performTextInput("Taglio Classico")

        composeTestRule.onNodeWithText("Durata (minuti)").performTextClearance()
        composeTestRule.onNodeWithText("Durata (minuti)").performTextInput("30")

        composeTestRule.onNodeWithText("Prezzo (€)").performTextClearance()
        composeTestRule.onNodeWithText("Prezzo (€)").performTextInput("15.0")

        // Simula il click sul pulsante "Aggiungi"
        composeTestRule.onNodeWithText("Annulla").performClick()

        // Verifica che i campi siano stati resettati
        composeTestRule.onNodeWithText("Nome").assert(hasText(""))
        composeTestRule.onNodeWithText("Descrizione").assert(hasText(""))
        composeTestRule.onNodeWithText("Durata (minuti)").assert(hasText(""))
        composeTestRule.onNodeWithText("Prezzo (€)").assert(hasText(""))
    }
}
