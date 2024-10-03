package com.example.marinobarbersalon

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow(User())
    val userState : StateFlow<User> = _userState.asStateFlow()

    init {
        checkAuthState()
    }

    fun checkAuthState(){
        if (auth.currentUser == null){
            _userState.value.state = AuthState.Unauthenticated
        }else{
            _userState.value.state = AuthState.Authenticated
        }
    }

    fun login(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _userState.value= _userState.value.copy(state = AuthState.Error("Email e password non possono essere vuoti"))
            return
        }
        _userState.value.state = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                }else{
                    _userState.value = _userState.value.copy(state = AuthState.Error(task.exception?.message?:"Errore"))
                }
            }
    }

    fun signup(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _userState.value = _userState.value.copy(state = AuthState.Error("Email e password non possono essere vuoti"))
            return
        }
        _userState.value.state = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    _userState.value = _userState.value.copy(state = AuthState.Authenticated)
                }else{
                    _userState.value = _userState.value.copy(state = AuthState.Error(task.exception?.message?:"Errore"))
                }
            }
    }

    fun logout(){
        auth.signOut()
        _userState.value = _userState.value.copy(state = AuthState.Unauthenticated)

    }


}

