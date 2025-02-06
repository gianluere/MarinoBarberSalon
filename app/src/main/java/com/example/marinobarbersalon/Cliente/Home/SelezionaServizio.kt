package com.example.marinobarbersalon.Cliente.Home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow


/**
 * Sono sostanzialmente uguali, vado a filtrare la lista dei servizi in base a quale tipologia ho scelto
 */


@Composable
fun SelezioneServizioCapelli(modifier: Modifier, viewModel: ListaServiziViewModel, onNavigateToSelezionaGiorno: (idSer: String) -> Unit) {

    val servizi by viewModel.listaServizi.collectAsState()


    Contenuto(modifier = modifier, servizi = servizi.filter{ it.tipo == "Capelli"}, titolo = "Capelli", onNavigateToSelezionaGiorno)


}

@Composable
fun SelezionaServiziobarba(modifier: Modifier, viewModel: ListaServiziViewModel, onNavigateToSelezionaGiorno: (idSer : String) -> Unit) {

    val servizi by viewModel.listaServizi.collectAsState()



    Contenuto(modifier = modifier, servizi = servizi.filter{ it.tipo == "Barba"}, "Barba", onNavigateToSelezionaGiorno)


}


@Composable
private fun Contenuto(
    modifier: Modifier, servizi: List<Servizio>,
    titolo : String,
    onNavigateToSelezionaGiorno: (idSer : String) -> Unit) {
    Column(modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        
        Text(text = "SEZIONE '$titolo'",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = my_white,
            modifier = Modifier.padding(top = 15.dp)
            )
        
        LazyColumn(
            contentPadding = PaddingValues(25.dp),
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            items(servizi) { servizio ->
                    CardAppuntamento(servizio = servizio, onNavigateToSelezionaGiorno)

            }
        }
    }
}


@Composable
fun CardAppuntamento(servizio : Servizio, onNavigateToSelezionaGiorno: (idSer : String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = my_yellow
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = {
            onNavigateToSelezionaGiorno(servizio.nome.toString())
        }
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
                Text(text = servizio.nome.toString(),
                    fontFamily = myFont,
                    fontSize = 19.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val icon = if (servizio.tipo == "Barba") {
                    Log.d("Messaggio", servizio.tipo)
                    R.drawable.barba_icona
                    } else {
                        if (servizio.nome?.contains("+") == true) {
                            Log.d("Messaggio", "Barba e capelli")
                            R.drawable.barba_e_capelli_512
                        } else {
                            Log.d("Messaggio", "Capelli")
                            R.drawable.capelli_icona
                        }
                    }

                Icon(painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(23.dp)
                        .padding(end = 4.dp),
                    tint = Color.Black)
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Black
            )

            Text(text = servizio.descrizione.toString(),
                modifier = Modifier.padding(horizontal = 5.dp),
                fontFamily = myFont,
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black)

            Spacer(Modifier.height(6.dp))

            Row(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom){
                Text(text = "Durata: ${servizio.durata} minuti",
                    fontFamily = myFont,
                    fontSize = 15.sp,
                    color = Color.Black)

                Text(text = String.format("%.2f", servizio.prezzo)+"€",
                    fontFamily = myFont,
                    fontSize = 14.sp,
                    color = Color.Black)
            }

        }
    }

}