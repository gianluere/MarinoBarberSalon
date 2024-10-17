package com.example.marinobarbersalon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_yellow

@Composable
fun Account(modifier: Modifier, userViewModel: UserViewModel) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "PANORAMICA",
                fontSize = 25.sp,
                fontFamily = myFont,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = my_yellow)
                    .padding(vertical = 3.dp)
            )

            Riga(testo = "Dati personali")
            Riga(testo = "Prenotazioni")
            Riga(testo = "Acquisti in app")
            Riga(testo = "Rilascia feedback")




        }
    }
}

@Composable
fun Contenuto() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "PANORAMICA",
                fontSize = 25.sp,
                fontFamily = myFont,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = my_yellow)
                    .padding(vertical = 3.dp)
            )
            
            Riga(testo = "Dati personali")
            Riga(testo = "Prenotazioni")
            Riga(testo = "Acquisti in app")
            Riga(testo = "Rilascia feedback")

            


        }
    }
    
}

@Composable
fun Riga(testo : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = my_yellow),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = testo,
            fontFamily = myFont,
            fontSize = 18.sp
        )

        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "rightArrow",
                modifier = Modifier.size(40.dp),
                tint = Color.Black)
        }
    }
}