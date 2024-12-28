package com.example.marinobarbersalon.Cliente.Account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow

@Composable
fun Recensioni(
    modifier: Modifier,
    listaRecensioniViewModel : ListaRecensioniViewModel,
    onNavigateToInserisciRecensione : () -> Unit,
    isAdmin : Boolean
) {

    listaRecensioniViewModel.caricaListaRecensioni()
    val listaRecensioni by listaRecensioniViewModel.listaRecensioni.collectAsState()



    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top
    ) {
        if(!isAdmin) {
            Box(
                modifier = Modifier.padding(top = 20.dp, end = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .background(color = my_yellow, shape = RoundedCornerShape(10.dp))
                        .height(45.dp)
                        .padding(horizontal = 5.dp)
                        .clickable {
                            onNavigateToInserisciRecensione()
                        },
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = null,
                        tint = my_grey,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "Inserisci recensione",
                        fontFamily = myFont,
                        color = my_grey,
                        fontSize = 20.sp
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(vertical = 20.dp)
        ) {
            items(listaRecensioni) { recensione ->
                CardRecensione(recensione)
            }
        }

    }

}


@Composable
fun CardRecensione(recensione : Recensione) {


    val stellePiene = recensione.stelle.toInt() // numero di stelle piene
    val mezzaStella = if (recensione.stelle - stellePiene >= 0.5) 1 else 0 // se c'è una mezza stella
    val stelleVuote = 5 - stellePiene - mezzaStella // numero di stelle vuote


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = my_yellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = recensione.nome,
            fontSize = 24.sp,
            fontFamily = myFont,
            color = my_grey
        )

        Row {

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

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = recensione.stelle.toString(),
                fontSize = 20.sp,
                fontFamily = myFont,
                color = my_grey
            )

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
            colors = CardDefaults.cardColors(
                containerColor = my_yellow
            )
        ){
            Text(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp).fillMaxSize(),
                text = recensione.descrizione,
                color = Color.Black,
                fontSize = 17.sp,
                fontFamily = myFont
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp,
            color = my_gold
        )
    }






}









