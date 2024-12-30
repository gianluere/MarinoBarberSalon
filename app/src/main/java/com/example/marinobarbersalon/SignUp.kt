package com.example.marinobarbersalon

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun SignUpScreen(navigaHome : () -> Unit, userViewModel: UserViewModel, distruzione : () -> Unit) {

    BackHandler {
       distruzione()
    }

    val userState by userViewModel.userState.collectAsState()

    var nome by remember {
        mutableStateOf("")
    }

    var cognome by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var eta by remember {
        mutableStateOf("")
    }

    var telefono by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }

    val image = if (passwordVisibility)
        painterResource(id = R.drawable.visibility_24dp_f5f5dc_fill0_wght400_grad0_opsz24)
    else
        painterResource(id = R.drawable.visibility_off_24dp_faf9f6_fill0_wght400_grad0_opsz24)

    val context = LocalContext.current
    LaunchedEffect(userState.state){
        when(userState.state){
            is AuthState.Authenticated -> navigaHome()
            is AuthState.Error -> Toast.makeText(context,
                (userState.state as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
            .padding(top = 30.dp, bottom = 50.dp),
        contentAlignment = Alignment.Center
    ){
        if (userState.state == AuthState.Loading){
            CircularProgressIndicator(color = my_gold)
        }else{
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "CREA ACCOUNT", color = my_white, fontSize = 30.sp)
                Text(text = "Inserisci le informazioni di seguito per completare la registrazione",Modifier.width(300.dp), color = my_white, textAlign = TextAlign.Center)

                Spacer(Modifier.height(50.dp))

                //Nome
                TextField(
                    value = nome,
                    onValueChange = {
                        if (it.length <= 20) {
                            nome = it
                        }
                    },
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 17.sp),
                    placeholder = { Text(text = "Nome", color = my_gold, fontFamily = myFont, fontSize = 17.sp) },

                    colors= TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_gold,
                        unfocusedTextColor = my_gold,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .width(280.dp)
                )

                Spacer(Modifier.height(16.dp))

                //Cognome
                TextField(
                    value = cognome,
                    onValueChange = {
                        if (it.length <= 20) {
                            cognome = it
                        }
                    },
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 17.sp),
                    placeholder = { Text(text = "Cognome", color = my_gold, fontFamily = myFont, fontSize = 17.sp) },

                    colors= TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_gold,
                        unfocusedTextColor = my_gold,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .width(280.dp)
                )

                Spacer(Modifier.height(16.dp))

                //Email
                TextField(
                    value = email,
                    onValueChange = {
                        if (it.length <= 35) {
                            email = it
                        }
                    },
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 17.sp),
                    placeholder = { Text(text = "Email", color = my_gold, fontFamily = myFont, fontSize = 17.sp) },

                    colors= TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_gold,
                        unfocusedTextColor = my_gold,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .width(280.dp)
                )

                Spacer(Modifier.height(16.dp))

                //Eta
                TextField(
                    value = eta,
                    onValueChange = {
                        if (it.length <= 2) {
                            eta = it
                        }
                    },
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 17.sp),
                    placeholder = { Text(text = "Età", color = my_gold, fontFamily = myFont, fontSize = 17.sp) },

                    colors= TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_gold,
                        unfocusedTextColor = my_gold,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .width(280.dp)
                )

                Spacer(Modifier.height(16.dp))

                //PAssword
                TextField(
                    value = password,
                    onValueChange = {password = it},
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 17.sp),
                    placeholder = { Text(text = "Password", color = my_gold, fontFamily = myFont, fontSize = 17.sp) },

                    colors= TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_gold,
                        unfocusedTextColor = my_gold,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ), trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility = !passwordVisibility
                        }) {
                            Image(image, contentDescription = "eye", colorFilter = ColorFilter.tint(my_white))

                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .width(280.dp)
                )

                Spacer(Modifier.height(16.dp))

                //Telefono
                TextField(
                    value = telefono,
                    onValueChange = {
                        if (it.length <= 14) {
                            telefono = it
                        }
                    },
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 17.sp, textAlign = TextAlign.Left),
                    placeholder = { Text(text = "Telefono, es: +39 1234567891", color = my_gold, fontFamily = myFont, fontSize = 17.sp) },

                    colors= TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = my_gold,
                        unfocusedTextColor = my_gold,
                        focusedIndicatorColor = my_white,
                        unfocusedIndicatorColor = my_white,
                        cursorColor = my_white
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .width(280.dp)
                )
                Spacer(Modifier.height(70.dp))
                Button(onClick = {
                    userViewModel.signup(email, password, nome, cognome, eta.toInt(), telefono)

                }, enabled = userState.state != AuthState.Loading, modifier = Modifier.width(230.dp), colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)) {
                    Text(text = "CONFERMA", color = my_gold, fontFamily = myFont, fontSize = 20.sp)
                }

            }
        }

    }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SignUpScreen()
}

 */