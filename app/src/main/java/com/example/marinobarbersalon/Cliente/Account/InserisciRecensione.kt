package com.example.marinobarbersalon.Cliente.Account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_yellow
import kotlin.math.roundToInt

@Composable
fun InserisciRecensione(modifier: Modifier = Modifier, listaRecensioniViewModel : ListaRecensioniViewModel) {

    var descrizione by rememberSaveable {
        mutableStateOf("")
    }

    var rating by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }

    val stellePiene = rating.toInt()
    val mezzaStella = if (rating - stellePiene >= 0.5) 1 else 0
    val stelleVuote = 5 - stellePiene - mezzaStella

    Column(
        modifier = modifier.size(300.dp).background(color = my_yellow).padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Inserisci recensione:",
            fontSize = 20.sp,
            fontFamily = myFont,
            color = my_grey,
        )

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = descrizione,
            onValueChange = {descrizione = it}
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            repeat(stellePiene) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Stella piena",
                    tint = my_gold
                )
            }

            // Mezza stella (opzionale)
            if (mezzaStella == 1) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.outline_star_half_24), // Puoi usare un'icona diversa per la mezza stella
                    contentDescription = "Mezza stella",
                    tint = my_gold // metà opacità per indicare la mezza stella
                )
            }

            // Stelle vuote
            repeat(stelleVuote) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.outline_star_border_24),
                    contentDescription = "Stella vuota",
                    tint = Color.Black
                )
            }
            /*
            Slider(
                value = rating.toFloat(),
                onValueChange = { newValue ->
                    rating = (newValue * 2).roundToInt() / 2.0 // Arrotonda al mezzo più vicino
                },
                valueRange = 0f..5.toFloat(),
                steps = (5 * 2) - 1, // Numero di step (0.5 incrementi)
                modifier = Modifier.fillMaxWidth(0.8f)
            )

             */

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                for (i in 1..5) {
                    // Calcola se mostrare la stella piena, mezza o vuota
                    val starRatingValue = i.toDouble()
                    val isFullStar = rating >= starRatingValue
                    val isHalfStar = rating in (starRatingValue - 0.5)..(starRatingValue - 0.1)

                    // Imposta l'icona e il colore della stella
                    Icon(
                        imageVector = when {
                            isFullStar -> Icons.Filled.Star
                            isHalfStar -> Icons.Filled.Star // Si può sostituire con un'icona di mezza stella
                            else -> Icons.Outlined.Call
                        },
                        contentDescription = null,
                        tint = if (isFullStar || isHalfStar) Color.Yellow else Color.Gray,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                // Cambia il rating al clic; aggiungi 0.5 per la mezza stella se cliccata
                                rating = if (rating == starRatingValue - 0.5) {
                                    starRatingValue // Da mezza a piena
                                } else {
                                    starRatingValue - 0.5 // Da piena a mezza
                                }

                            }
                    )
                }
            }





        }

    }

}