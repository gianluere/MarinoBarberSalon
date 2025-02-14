package com.example.marinobarbersalon

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Admin.AdminViewModel
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.example.marinobarbersalon.Cliente.Home.UserViewModel


import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    adminViewModel: AdminViewModel,
    navigaHomeCliente: () -> Unit,
    navigaHomeAdmin: () -> Unit,
    navigaSignUp: () -> Unit
) {
    val userState by userViewModel.userState.collectAsState()
    val adminState by adminViewModel.adminState.collectAsState()
    val userValidationMessage by userViewModel.validationMessage.collectAsState()
    val adminValidationMessage by adminViewModel.validationMessage.collectAsState()

    val loginRepository = LoginRepository()
    var isLoading by remember { mutableStateOf(false) }


    val context = LocalContext.current
    BackHandler {
        val activity = context as? ComponentActivity
        activity?.finish()
    }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val image = if (passwordVisibility)
        painterResource(id = R.drawable.visibility_24dp_f5f5dc_fill0_wght400_grad0_opsz24)
    else
        painterResource(id = R.drawable.visibility_off_24dp_faf9f6_fill0_wght400_grad0_opsz24)

    val focusManager = LocalFocusManager.current

    //Mi permette di fare azioni al cambio di stato di queste variabili
    LaunchedEffect(userState.state, adminState.state, userValidationMessage, adminValidationMessage) {

        if (!adminValidationMessage.isNullOrEmpty()) {
            Toast.makeText(context, adminValidationMessage, Toast.LENGTH_SHORT).show()
            adminViewModel.resetValidationMessage() // Resetta il messaggio
        }

        if (!userValidationMessage.isNullOrEmpty()) {
            Toast.makeText(context, userValidationMessage, Toast.LENGTH_SHORT).show()
            userViewModel.resetValidationMessage() // Resetta il messaggio
        }

        when (adminState.state) {
            is AuthState.Authenticated -> navigaHomeAdmin() //navigo home admin
            else -> when (userState.state) {
                is AuthState.Authenticated -> navigaHomeCliente() //navigo home cliente
                else -> Unit
            }
        }
    }


    //serie di condizioni per mostrare il CircularProgressIndicator per il caricamento login di firebase
    if (userState.state == null || userState.state == AuthState.Authenticated || userState.state == AuthState.Loading ||
        adminState.state == AuthState.Authenticated || adminState.state == AuthState.Authenticated || isLoading ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(my_grey),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp),
                color = my_gold
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF333333))
                .padding(top = 30.dp)
                .pointerInput(Unit) {
                    // Quando l'utente tocca, rimuove il focus dai campi di testo
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
            ,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50.dp))
                        .background(color = my_grey)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo3),
                        contentDescription = "logo",
                        Modifier
                            .height(180.dp)
                            .aspectRatio(16 / 9f)
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(70.dp))
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        userViewModel.resetValidationMessage()
                        adminViewModel.resetValidationMessage()
                    },
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 22.sp),
                    placeholder = {
                        Text(
                            text = "Email",
                            color = Color(0xFFF5F5DC),
                            fontFamily = myFont,
                            fontSize = 22.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_white,
                        unfocusedTextColor = my_white,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(60.dp))

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        userViewModel.resetValidationMessage()
                        adminViewModel.resetValidationMessage()
                    },
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 22.sp),
                    placeholder = {
                        Text(
                            text = "Password",
                            color = Color(0xFFF5F5DC),
                            fontFamily = myFont,
                            fontSize = 22.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_white,
                        unfocusedTextColor = my_white,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Image(
                                image,
                                contentDescription = "eye",
                                colorFilter = ColorFilter.tint(my_white)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(), //per mostrare caratteri password
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))




                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {


                        isLoading = true

                        //meccanismo per effettuare il login e riconoscere il tipo di utente
                        loginRepository.loginUser(email, password) { userId, error ->
                            if (userId != null) {
                                loginRepository.determineUserType { userType ->
                                    when (userType) {
                                        "admin" -> {
                                            adminViewModel.checkAuthState()
                                        }
                                        "cliente" -> {
                                            userViewModel.checkAuthState()
                                        }
                                        else -> {
                                            Log.e("MainActivity", "Unknown user type")
                                        }
                                    }
                                }
                                isLoading = false
                            } else {
                                userViewModel.login(email, password)
                                isLoading = false
                            }
                        }

                    },
                    enabled = !isLoading,//userState.state != AuthState.Loading && adminState.state != AuthState.Loading,
                    modifier = Modifier.width(230.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux, disabledContainerColor = my_bordeaux),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "ACCEDI",
                        color = my_gold,
                        fontFamily = myFont,
                        fontSize = 20.sp,

                    )
                }

                TextButton(onClick = { navigaSignUp() }) {
                    Text(
                        text = "Non hai un account, registrati",
                        fontFamily = myFont,
                        fontSize = 18.sp,
                        color = my_gold,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }


        }
    }


}

