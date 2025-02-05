package com.example.marinobarbersalon.Cliente.Account

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import kotlinx.coroutines.launch

@Composable
fun DatiPersonali(modifier: Modifier, userViewModel: UserViewModel) {

    val userState by userViewModel.userState.collectAsState()

    var readOnly by remember {
        mutableStateOf(true)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var nome by remember {
        mutableStateOf(userState.nome.toString())
    }

    var cognome by remember {
        mutableStateOf(userState.cognome.toString())
    }

    var email by remember {
        mutableStateOf(userState.email.toString())
    }

    var eta by remember {
        mutableStateOf(userState.eta.toString())
    }

    var telefono by remember {
        mutableStateOf(userState.telefono.toString())
    }

    var password by remember {
        mutableStateOf(userState.password.toString())
    }

    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    LaunchedEffect(readOnly) {
        if (!readOnly) {
            focusRequester.requestFocus()
        }
    }



    Box(
        modifier = modifier.fillMaxSize()
            .pointerInput(Unit) {
                // Quando l'utente tocca, rimuove il focus dai campi di testo
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = if (readOnly){
                        {
                            readOnly = !readOnly
                        }
                    }else {
                        {
                            loading = true
                            userViewModel.updateDati(
                                nome, cognome, eta.toInt(), telefono, callback = {
                                    loading = false
                                    readOnly = !readOnly
                                }
                            )
                        }
                    }
                ) {
                    if(readOnly){
                        Icon(
                            Icons.Outlined.Edit,
                            null,
                            tint = my_bordeaux,
                            modifier = Modifier.size(30.dp)

                        )
                    }else{
                        Icon(
                            Icons.Outlined.Done,
                            null,
                            tint = Color(0xFF50C878),
                            modifier = Modifier.size(30.dp)

                        )
                    }

                }
            }


            RigaDato(
                label = "Nome",
                value = nome,
                onValueChange = {nome = it},
                readOnly = readOnly,
                focusRequester = if (!readOnly) {focusRequester} else {null}
            )

            RigaDato(
                label = "Cognome",
                value = cognome,
                onValueChange = {cognome = it},
                readOnly = readOnly
            )

            RigaDato(
                label = "Email",
                value = email,
                onValueChange = {email = it},
                readOnly = true
            )

            RigaDato(
                label = "Età",
                value = eta,
                onValueChange = {eta = it},
                readOnly = readOnly
            )

            RigaDato(
                label = "Telefono",
                value = telefono,
                onValueChange = {telefono = it},
                readOnly = readOnly,
                onFocusChanged = { focused ->
                    if (focused.isFocused) {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                }
            )



        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(my_grey.copy(alpha = 0.6f))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp),
                    color = my_gold
                )
            }
        }
    }


}


@Composable
fun RigaDato(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean,
    onFocusChanged: ((FocusState) -> Unit)? = null,
    focusRequester: FocusRequester? = null
) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ",
                color = my_white,
                fontSize = 24.sp,
                fontFamily = myFont
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                textStyle = TextStyle(fontFamily = myFont, fontSize = 24.sp, color = my_white),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .onFocusChanged { focusState ->
                    onFocusChanged?.invoke(focusState) // Invoca onFocusChanged se è definito
                    }
                    .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
                singleLine = true // Evita di andare a capo
            )
            /*
            TextField(
                modifier = Modifier.padding(vertical = 0.dp),
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontFamily = myFont, fontSize = 20.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = my_white,
                    unfocusedTextColor = my_white,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = my_white
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 1,
                readOnly = readOnly
            )

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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    maxLines = 1
                )

             */
        }
        HorizontalDivider(
            Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = my_white
        )
    }
}