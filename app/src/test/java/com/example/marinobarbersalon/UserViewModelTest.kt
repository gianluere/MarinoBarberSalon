package com.example.marinobarbersalon

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserViewModelTest {



    private lateinit var loginRepository: LoginRepository
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        loginRepository = LoginRepository()
        //userViewModel = viewModel()


    }

    @Test
    fun tryLogin(){



        assertEquals(userViewModel.userState.value.nome, "Gianluca")

    }

}