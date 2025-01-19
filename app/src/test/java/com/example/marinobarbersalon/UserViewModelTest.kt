package com.example.marinobarbersalon

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

class UserViewModelTest {



    private lateinit var loginRepository: LoginRepository
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        loginRepository = LoginRepository()
        userViewModel = UserViewModel()

        loginRepository.loginUser("gianlucaeremita.03@gmail.com", "gianluca") { userId, error ->

        }


    }

    @Test
    fun tryLogin(){

        userViewModel.checkAuthState()
        assertEquals(userViewModel.userState.value.nome, "Gianluca")

    }

}