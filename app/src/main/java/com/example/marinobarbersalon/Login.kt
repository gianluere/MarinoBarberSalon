package com.example.marinobarbersalon

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun LoginScreen(navController : NavController, adminNavController : NavController, userViewModel: UserViewModel, adminViewModel: AdminViewModel) {
    val context = LocalContext.current
    BackHandler {
        val activity = context as? ComponentActivity
        activity?.finish()
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }

    val image = if (passwordVisibility)
        painterResource(id = R.drawable.visibility_24dp_f5f5dc_fill0_wght400_grad0_opsz24)
    else
        painterResource(id = R.drawable.visibility_off_24dp_faf9f6_fill0_wght400_grad0_opsz24)

    val userState by userViewModel.userState.collectAsState()
    val adminState by adminViewModel.adminState.collectAsState()



    LaunchedEffect(userState.state, adminState.state){
        when(userState.state){
            is AuthState.Authenticated -> {
                navController.navigate("home"){
                    launchSingleTop = true
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> Toast.makeText(context,
                (userState.state as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
        when(adminState.state){
            is AuthState.Authenticated -> {
                adminNavController.navigate("prova"){
                    launchSingleTop = true
                }
            }
            is AuthState.Error -> Toast.makeText(context,
                (userState.state as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
            .padding(top = 30.dp),
    ){
        Column(
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(color = Color(0xFF7B2B31))){
                Image(painter = painterResource(id = R.drawable.logo_scontornato), contentDescription ="logo",
                    Modifier
                        .height(180.dp)
                        .aspectRatio(16 / 9f)
                        .align(Alignment.Center))
            }
            Spacer(modifier = Modifier.height(70.dp))
            TextField(value = email, onValueChange = {email = it}, textStyle = TextStyle(fontFamily = myFont, fontSize = 22.sp), placeholder = { Text(text = "Email", color = Color(0xFFF5F5DC), fontFamily = myFont, fontSize = 22.sp) },
                colors= TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = my_white,
                    unfocusedTextColor = my_white,
                    focusedIndicatorColor = my_white,
                    unfocusedIndicatorColor = my_white,
                    cursorColor = my_white
                ), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(60.dp))

            TextField(value = password, onValueChange = {password = it}, textStyle = TextStyle(fontFamily = myFont, fontSize = 22.sp), placeholder = { Text(text = "Password", color = Color(0xFFF5F5DC), fontFamily = myFont,  fontSize = 22.sp) },
                colors= TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = my_white,
                    unfocusedTextColor = my_white,
                    focusedIndicatorColor = my_white,
                    unfocusedIndicatorColor = my_white,
                    cursorColor = my_white
                ), trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Image(image, contentDescription = "eye", colorFilter = ColorFilter.tint(my_white))

                    }
                }, visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(onClick = { if(adminViewModel.isAdmin(email)){
                adminViewModel.login(email, password)
            }
                else {
                userViewModel.login(email, password)
            }
            }, enabled = userState.state != AuthState.Loading,
                modifier = Modifier.width(230.dp), colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)) {
                Text(text = "ACCEDI", color = my_gold, fontFamily = myFont, fontSize = 20.sp)
            }

            TextButton(onClick = { navController.navigate("signup") }) {
                Text(text = "Non hai un account, registrati", fontFamily = myFont, color = my_gold, textDecoration = TextDecoration.Underline)
            }
        }

    }

}
