package com.example.marinobarbersalon.Cliente.Account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow

@Composable
fun Account(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    notificheClienteViewModel : NotificheClienteViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigaDatiPersonali : () -> Unit,
    onNavigaPrenotazioni : () -> Unit,
    onNavigaProdottiPrenotati : () -> Unit,
    onNavigaRecensioni : () -> Unit
) {


    val prenotazioni by notificheClienteViewModel.notifichePrenotazioni.collectAsState()

    val prodottiPrenotati by notificheClienteViewModel.notificheProdotti.collectAsState() //by userViewModel.listaProdottiPrenotati.collectAsState()


    val userState by userViewModel.userState.collectAsState()

    LaunchedEffect(userState.state){
        when(userState.state){
            is AuthState.Unauthenticated -> onNavigateToLogin()
            else -> Unit
        }
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = my_yellow),
                contentAlignment = Alignment.CenterStart
            ){
                Text(text = "PANORAMICA",
                    fontSize = 25.sp,
                    fontFamily = myFont,
                    color = my_grey,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )

            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color.Gray
            )

            Riga(testo = "Dati personali", modifier = Modifier.clickable { onNavigaDatiPersonali() })
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = my_gold
            )

            Riga(testo = "Prenotazioni", modifier = Modifier.clickable { onNavigaPrenotazioni() }, notifiche= prenotazioni)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = my_gold
            )

            Riga(testo = "Acquisti in app",modifier = Modifier.clickable { onNavigaProdottiPrenotati() }, notifiche = prodottiPrenotati)//listaProdottiPrenotati.size)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = my_gold
            )

            Riga(testo = "Rilascia feedback", modifier = Modifier.clickable { onNavigaRecensioni() })
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = my_gold
            )

            Box(
                modifier = Modifier.fillMaxSize().padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomCenter,
            ){
                Button(
                    onClick = {
                        userViewModel.logout()
                    },

                    modifier = Modifier
                        .padding(8.dp)
                        .background(my_bordeaux, shape = RoundedCornerShape(17.dp)) //Sfondo!!!
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = my_white
                    ),
                    shape = RoundedCornerShape(17.dp),
                    enabled = (userState.state != AuthState.Loading)
                ) {
                    Text(text = "LOGOUT", color = my_gold, fontFamily = myFont, fontSize = 20.sp)
                }
            }






        }
    }
}

@Composable
fun Riga(testo : String, modifier : Modifier, notifiche : Int = 0) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = my_yellow),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(text = testo,
                fontFamily = myFont,
                fontSize = 19.sp,
                color = my_grey,
                modifier = Modifier.padding(start = 7.dp)
            )

            if(notifiche > 0){
                Box(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(25.dp)
                        .clip(CircleShape) // Applica la forma circolare
                        .background(color = my_bordeaux), // Imposta il colore di sfondo
                    contentAlignment = Alignment.Center // Centra il testo all'interno
                ) {
                    Text(
                        text = notifiche.toString(),
                        color = my_gold, // Colore del testo
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }

        }



        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "rightArrow",
            modifier = Modifier.size(35.dp),
            tint = Color.Black)

    }
}