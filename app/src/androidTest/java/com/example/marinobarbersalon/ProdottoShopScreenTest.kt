package com.example.marinobarbersalon

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.marinobarbersalon.Cliente.Shopping.Shop
import com.example.marinobarbersalon.Cliente.Shopping.ListaProdottiViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: ListaProdottiViewModel

    @Before
    fun setUp() {
        viewModel = ListaProdottiViewModel()
    }

    @Test
    fun testSelezioneProdottoNavigaASchermataPrenotazione() {
        var prodottoSelezionato = ""

        // Imposta la schermata Shop
        composeTestRule.setContent {
            Shop(
                modifier = Modifier,
                title = "Barba",
                onNavigateToProdottoShop = { nomeProdotto ->
                    prodottoSelezionato = nomeProdotto
                }
            )
        }

        // Aspetta che la UI sia pronta
        composeTestRule.waitForIdle()

        // Scorri fino al prodotto "Provba"
        composeTestRule.onNode(hasText("Provba"), useUnmergedTree = true)
            .performScrollTo()

        // Cerca il prodotto "Provba" nella lista e clicca
        composeTestRule.onNodeWithText("Provba", useUnmergedTree = true)
            .assertExists("Il prodotto 'Provba' non esiste nella UI")
            .performClick()

        // Aspetta la navigazione alla schermata di prenotazione
        composeTestRule.waitForIdle()

        // Verifica che la schermata di prenotazione sia visibile
        composeTestRule.onNodeWithTag("prenotaprodotto")
            .assertExists("La schermata di prenotazione non si è aperta correttamente")
    }


}
