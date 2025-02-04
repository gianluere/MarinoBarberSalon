package com.example.marinobarbersalon

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.Cliente.Shopping.Shop
import com.example.marinobarbersalon.Cliente.Shopping.ListaProdottiViewModel
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoShop
import com.example.marinobarbersalon.Cliente.Shopping.SelezionaShop
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.concurrent.timer

class ShopScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: ListaProdottiViewModel

    @Before
    fun setUp() {
        viewModel = ListaProdottiViewModel()
    }

    @Test
    fun prenotazioneProdottoAvvenutaConSuccesso() {
        val auth = Firebase.auth

        // Accedi con un utente di test
        auth.signInWithEmailAndPassword("testuser@example.com", "password123")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    composeTestRule.waitUntil(timeoutMillis = 5000) {
                        auth.currentUser != null
                    }


                    composeTestRule.setContent {
                        ProdottoShop(
                            modifier = Modifier,
                            nomeProdotto = "Crema ricci"
                        ) {
                            composeTestRule.onNodeWithTag("PRENOTA")
                                .assertExists("Il pulsante non esiste nella UI")
                                .performClick()

                            composeTestRule.onNodeWithText("Prodotto prenotato \ncon successo")
                                .assertIsDisplayed()
                        }
                    }
                } else {
                    throw AssertionError("Autenticazione fallita: ${task.exception?.message}")
                }
            }
    }
}
