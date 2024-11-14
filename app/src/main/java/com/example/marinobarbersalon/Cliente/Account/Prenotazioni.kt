package com.example.marinobarbersalon.Cliente.Account

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Cliente.Home.CardAppuntamento
import com.example.marinobarbersalon.Cliente.Home.Servizio
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun Prenotazioni(modifier : Modifier, userViewModel: UserViewModel) {

    userViewModel.sincronizzaPrenotazioni()
    val userState by userViewModel.userState.collectAsState()
    val listaPrenotazioni by userViewModel.listaAppuntamenti.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Le tue prenotazioni:",
            color = my_white,
            fontSize = 25.sp,
            fontFamily = myFont,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(25.dp),
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            items(listaPrenotazioni) { appuntamento ->
                CardPrenotazione(appuntamento = appuntamento)

            }
        }
    }

}



@Composable
fun CardPrenotazione(appuntamento: Appuntamento) {
    val oggi = LocalDate.now()
    val ora = LocalTime.now()
    val giornoApp = LocalDate.parse(appuntamento.data, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    val oraApp = LocalTime.of(appuntamento.orarioInizio.take(2).toInt(), appuntamento.orarioInizio.substring(3, 5).toInt())

    var attivo = false

    if (oggi.isBefore(giornoApp)){
        attivo = true
    }else if (oggi.isEqual(giornoApp)){
        if (ora.isBefore(oraApp)){
            attivo = true
        }
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (attivo) my_yellow else Color(0xFF999999)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {



        Column(
            Modifier.padding(top = 6.dp, bottom = 2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start

        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = appuntamento.servizio,
                    fontFamily = myFont,
                    fontSize = 19.sp,
                    color = Color.Black
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Black
            )

            Text(text = appuntamento.descrizione,
                modifier = Modifier.padding(horizontal = 5.dp),
                fontFamily = myFont,
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                color = Color.Black
            )

            Spacer(Modifier.height(6.dp))

            //val data = LocalDate.parse(appuntamento.data, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom){
                Text(
                    text = "Data: ${appuntamento.data}",
                    fontFamily = myFont,
                    fontSize = 15.sp,
                    color = Color.Black
                )

                Text(
                    text = appuntamento.orarioInizio + " - " + appuntamento.orarioFine,
                    fontFamily = myFont,
                    fontSize = 15.sp,
                    color = Color.Black
                )

                Text(
                    text = String.format("%.2f", appuntamento.prezzo)+"€",
                    fontFamily = myFont,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

        }
    }

}