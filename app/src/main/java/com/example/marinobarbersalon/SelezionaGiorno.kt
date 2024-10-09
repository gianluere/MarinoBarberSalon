package com.example.marinobarbersalon

import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_yellow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun SelezionaGiorno(listaServiziViewModel: ListaServiziViewModel,
                    onBack : () -> Unit,
                    idSer : String,
                    onNavigateToRiepilogo : (String, String, String, String) -> Unit) {
    
    ScaffoldPersonalizzato(
        titolo = "Scegli un giorno e un orario",
        onBack = {
            onBack()
        },
        content = {
            Data(idSer, listaServiziViewModel, onNavigateToRiepilogo)
        }
        )

}

@Composable
fun Data(idSer : String, listaServViewModel: ListaServiziViewModel, onNavigateToRiepilogo : (String, String, String, String) -> Unit) {

    Log.d("PROVA", idSer)
    val listaServiziViewModel = listaServViewModel
    val listaServizi by listaServiziViewModel.listaServizi.collectAsState()

    Log.d("PROVA", "sono qui")
    Log.d("PROVA", "Contenuto listaServizi: $listaServizi")
    val servizio = listaServizi.find { serv->
        serv.nome == idSer
    }
    Log.d("PROVA", "sono sotto")
    if (servizio != null) {
        Log.d("FINALE", servizio.nome.toString())
    }
    val listaGiorniViewModel : ListaGiorniViewModel = viewModel(
        factory = servizio?.let { ListaGiorniViewModelFactory(it) }
    )

    val listaGiorni by listaGiorniViewModel.listaGiorniAggiornata.collectAsState()

    var indexGiornoSelezionato by rememberSaveable {
        mutableIntStateOf(0)
    }

    var indexOrarioSelezionato by rememberSaveable {
        mutableIntStateOf(0)
    }

    var bottone by rememberSaveable {
        mutableStateOf(false)
    }

    var dataSelezionata by rememberSaveable {
        mutableStateOf(LocalDate.now())
    }
    var orarioInizio : LocalTime = LocalTime.now()
    var orarioFine : LocalTime = LocalTime.now()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 30.dp)) {
        LazyRow(
            modifier = Modifier.background(color = Color(0xFFF5F5DC)),
            contentPadding = PaddingValues(25.dp),
            horizontalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            items(listaGiorni.size){index ->
                CardGiorno(
                    giorno = listaGiorni[index].first,
                    index = index,
                    isSelected = index == indexGiornoSelezionato,
                    onCardSelected = { selectedIndex ->
                        indexGiornoSelezionato = selectedIndex
                        indexOrarioSelezionato = 0
                        dataSelezionata = listaGiorni[indexGiornoSelezionato].first
                        Log.d("Servizio", dataSelezionata.toString())
                    }
                )

            }
        }

        Spacer(Modifier.height(30.dp))

        Box(modifier = Modifier
            .padding(horizontal = 8.dp)
            .weight(1f)){

            Box(modifier = Modifier
                .fillMaxSize()
                .border(2.dp, my_gold, RoundedCornerShape(17.dp))
                .background(color = my_yellow, RoundedCornerShape(17.dp)),
                contentAlignment = Alignment.Center){

                val orariDisponibili = listaGiorni[indexGiornoSelezionato].second

                if (orariDisponibili.isEmpty()){
                    Text(text = "NESSUN ORARIO DISPONIBILE\n" +
                            "\n" +
                            "SELEZIONARE UN’ALTRA DATA",
                        fontSize = 23.sp,
                        fontFamily = myFont,
                        color = Color.Black,
                        lineHeight = 14.sp
                        )
                    bottone = false
                }else{
                    LazyColumn(
                        contentPadding = PaddingValues(25.dp),
                        verticalArrangement = Arrangement.spacedBy(23.dp)
                    ) {
                        items(orariDisponibili.size) { index ->
                            if (index == 0){
                                orarioInizio = orariDisponibili[indexOrarioSelezionato].first
                                orarioFine = orariDisponibili[indexOrarioSelezionato].second
                            }
                            CardOrario(
                                orario = orariDisponibili[index],
                                index = index,
                                indexGiornoSelezionato = indexGiornoSelezionato,
                                isSelected = index == indexOrarioSelezionato,
                                onCardSelected = { selectedIndex ->
                                    indexOrarioSelezionato = selectedIndex
                                    orarioInizio = orariDisponibili[indexOrarioSelezionato].first
                                    orarioFine = orariDisponibili[indexOrarioSelezionato].second
                                }
                            )

                        }
                    }
                    bottone = true
                }



            }


        }
        
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
            Log.d("Servizio", "data scritta $dataSelezionata")
            onNavigateToRiepilogo(idSer, orarioInizio.toString(), orarioFine.toString(), dataSelezionata.toString())
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 9.dp, start = 14.dp, end = 14.dp),
            enabled = bottone,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)) {
            Text(text = "PROSEGUI", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
        }


    }


}


@Composable
fun CardGiorno(giorno : LocalDate, index : Int, isSelected: Boolean, onCardSelected: (Int) -> Unit) {

    Card(
        modifier = Modifier
            .size(90.dp)
            .border(if (isSelected) 4.dp else 2.dp, Color.Black, RoundedCornerShape(17.dp))
            .clickable {
                onCardSelected(index)
            },
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) Color(0xFFB4915B) else my_gold
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = giorno.dayOfMonth.toString(),
                fontFamily = myFont,
                color = Color.Black,
                fontSize = 17.sp)

            Text(text = giorno.format(DateTimeFormatter.ofPattern("MMMM", Locale("it"))).replaceFirstChar { it.uppercase() },
                fontFamily = myFont,
                color = Color.Black,
                fontSize = 17.sp)

            Text(text = giorno.format(DateTimeFormatter.ofPattern("EEE", Locale("it"))).uppercase(),
                fontFamily = myFont,
                color = Color.Black,
                fontSize = 17.sp)
        }
    }
}


@Composable
fun CardOrario(orario : Pair<LocalTime, LocalTime>, index: Int, indexGiornoSelezionato : Int, isSelected: Boolean, onCardSelected: (Int) -> Unit) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(if (isSelected) 4.dp else 2.dp, Color.Black, RoundedCornerShape(17.dp))
            .clickable {
                onCardSelected(index)
            },
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) Color(0xFFB4915B) else my_gold
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            val stringa = orario.first.toString() + " - " + orario.second.toString()
            Text(text = stringa,
                fontFamily = myFont,
                color = Color.Black,
                fontSize = 17.sp)
        }

    }

}