package com.example.marinobarbersalon

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Riepilogo(userViewModel: UserViewModel,
              listaServiziViewModel: ListaServiziViewModel,
              idSer : String,
              orarioInizio : String,
              orarioFine : String,
              dataSelezionata : String,
              onBack : () -> Unit
) {
    Log.d("Input", idSer)
    ScaffoldPersonalizzato(titolo = "Riepilogo appuntamento",
        onBack = onBack,
        content = {
            Contenuto(userViewModel, listaServiziViewModel, idSer, orarioInizio, orarioFine, dataSelezionata)
        })
}


@Composable
fun Contenuto(
    userViewModel: UserViewModel,
    listaServViewModel: ListaServiziViewModel,
    idSer: String,
    orarioInizio : String,
    orarioFine : String,
    dataSelezionata: String
) {

    val listaServiziViewModel = listaServViewModel
    val listaServizi by listaServiziViewModel.listaServizi.collectAsState()
    val servizio = listaServizi.find { serv->
        serv.nome == idSer
    }

    val user = userViewModel.userState.collectAsState()
    val dataSelezionata = LocalDate.parse(dataSelezionata).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 20.dp, start = 14.dp, end = 14.dp, bottom = 10.dp)
    ) {
        Text(text = "Servizio: " + servizio!!.nome,
            color = my_gold,
            fontFamily = myFont,
            fontSize = 25.sp
        )
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp), thickness = 2.dp, color = my_white)

        Text(modifier = Modifier.fillMaxWidth(),
            text = "Durata: ${servizio.durata} minuti",
            color = my_gold,
            fontFamily = myFont,
            fontSize = 25.sp
        )
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp), thickness = 2.dp, color = my_white)

        Text(modifier = Modifier.fillMaxWidth(),
            text = "Data: $dataSelezionata",
            color = my_gold,
            fontFamily = myFont,
            fontSize = 25.sp
        )
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp), thickness = 2.dp, color = my_white)

        Text(modifier = Modifier.fillMaxWidth(),
            text = "Ora: $orarioInizio - $orarioFine",
            color = my_gold,
            fontFamily = myFont,
            fontSize = 25.sp
        )
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp), thickness = 2.dp, color = my_white)

        Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End){
            Text(modifier = Modifier.padding(bottom = 10.dp),
                text = "Totale: " + String.format("%.2f", servizio.prezzo) + "€",
                color = my_gold,
                fontSize = 25.sp,
                fontFamily = myFont,
                textDecoration = TextDecoration.Underline
            )
            Button(onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)) {
                Text(text = "PROSEGUI", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
            }
        }


    }


}