package com.example.marinobarbersalon

import android.app.Activity
import android.app.Application
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.example.marinobarbersalon.Cliente.Home.HomeClienteActivity
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignUpActivity>()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        viewModel = UserViewModel()
    }


    @Test
    fun registrazioneAvvenutaConSuccessoUI() {
        val scenario = ActivityScenario.launch(SignUpActivity::class.java)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Nome").performTextInput("Mario")
        composeTestRule.onNodeWithText("Cognome").performTextInput("Rossi")
        composeTestRule.onNodeWithText("Email").performTextInput("mario.rossi5@esempio.com")
        composeTestRule.onNodeWithText("Età").performTextInput("20")
        composeTestRule.onNodeWithText("Telefono").performTextInput("1234567890")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")

        composeTestRule.onNodeWithText("CONFERMA").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithTag("Home").fetchSemanticsNodes().isNotEmpty()
        }

        scenario.close()
    }

    @Test
    fun registrazioneFallisceConEmailEsistente() {
        val scenario = ActivityScenario.launch(SignUpActivity::class.java)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Nome").performTextInput("Mario")
        composeTestRule.onNodeWithText("Cognome").performTextInput("Rossi")
        composeTestRule.onNodeWithText("Email").performTextInput("mario.rossi5@esempio.com")
        composeTestRule.onNodeWithText("Età").performTextInput("20")
        composeTestRule.onNodeWithText("Telefono").performTextInput("1234567890")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")

        composeTestRule.onNodeWithText("CONFERMA").performClick()

        composeTestRule.onNodeWithTag("Home").assertDoesNotExist()

        scenario.close()
    }

    @Test
    fun registrazioneFallisceConPasswordTroppoCorta() {
        val scenario = ActivityScenario.launch(SignUpActivity::class.java)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Nome").performTextInput("Mario")
        composeTestRule.onNodeWithText("Cognome").performTextInput("Rossi")
        composeTestRule.onNodeWithText("Email").performTextInput("mario.rossi@esempio.com")
        composeTestRule.onNodeWithText("Età").performTextInput("20")
        composeTestRule.onNodeWithText("Telefono").performTextInput("1234567890")
        composeTestRule.onNodeWithText("Password").performTextInput("a")

        composeTestRule.onNodeWithText("CONFERMA").performClick()

        composeTestRule.waitForIdle()


        composeTestRule.onNodeWithTag("Home").assertDoesNotExist()

        scenario.close()
    }

    @Test
    fun registrazioneFallisceConEmailNonValida() {
        val scenario = ActivityScenario.launch(SignUpActivity::class.java)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Nome").performTextInput("Mario")
        composeTestRule.onNodeWithText("Cognome").performTextInput("Rossi")
        composeTestRule.onNodeWithText("Email").performTextInput("email-non-valida")
        composeTestRule.onNodeWithText("Età").performTextInput("20")
        composeTestRule.onNodeWithText("Telefono").performTextInput("1234567890")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")

        composeTestRule.onNodeWithText("CONFERMA").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("Home").assertDoesNotExist()

        scenario.close()
    }







}

