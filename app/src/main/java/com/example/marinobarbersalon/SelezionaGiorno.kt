package com.example.marinobarbersalon

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_yellow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


@Composable
fun SelezionaGiorno(onBack : () -> Unit) {
    
    ScaffoldPersonalizzato(
        titolo = "Scegli un giorno e un orario",
        onBack = {
            onBack()
        },
        content = {
            Data()
        }
        )

}


fun generaListaDate(dataInizio : LocalDate, dataFine : LocalDate): List<LocalDate> {
    val listaDate = mutableListOf<LocalDate>()
    var dataCorrente = dataInizio

    while (dataCorrente.isBefore(dataFine) || dataCorrente.isEqual(dataFine)) {
        listaDate.add(dataCorrente)
        // Aggiungi 1 giorno
        dataCorrente = dataCorrente.plus(1, ChronoUnit.DAYS)
    }

    return listaDate
}

val listaDate = generaListaDate(LocalDate.now(), LocalDate.now().plusMonths(2))



fun generateListaDate(
    oggi: LocalDate,
    giorniTotali: Int,
    giorniFestivi: List<LocalDate>
): List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>> {
    val giorniDisponibili = mutableListOf<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>()

    val orarioInizio = LocalTime.of(9, 0)
    val orarioFine = LocalTime.of(20, 0)
    val durataSlot = 30L

    for (i in 0..giorniTotali) {
        val giornoCorrente = oggi.plusDays(i.toLong())

        // Escludi sabato, domenica e giorni festivi
        if (giornoCorrente.dayOfWeek == DayOfWeek.SATURDAY ||
            giornoCorrente.dayOfWeek == DayOfWeek.SUNDAY ||
            giorniFestivi.contains(giornoCorrente)) {
            // Aggiungi il giorno senza orari disponibili
            giorniDisponibili.add(giornoCorrente to emptyList())
        } else {
            val slotOrari = mutableListOf<Pair<LocalTime, LocalTime>>()

            var orarioCorrente = orarioInizio
            while (orarioCorrente.isBefore(orarioFine)) {
                val orarioSuccessivo = orarioCorrente.plusMinutes(durataSlot)
                slotOrari.add(orarioCorrente to orarioSuccessivo)
                orarioCorrente = orarioSuccessivo
            }

            giorniDisponibili.add(giornoCorrente to slotOrari)
        }
    }

    return giorniDisponibili
}


val giorniFestivi = listOf(
    LocalDate.of(2024, 1, 1),  // Capodanno
    LocalDate.of(2024, 12, 25), // Natale
    LocalDate.of(2024, 12, 26), // Santo Stefano
    LocalDate.of(2024, 4, 25),  // Festa della Liberazione
    LocalDate.of(2024, 8, 15)   // Ferragosto
)

val giorni = generateListaDate(
    LocalDate.now(),
    60, giorniFestivi
)


/*
fun generaListaOccupati(oggi: LocalDate,
                        giorniTotali: Int)
: List<Pair<LocalDate, List<Pair<LocalTime, LocalTime>>>>{
    val db = Firebase.firestore

    val listaOccupati = mutableListOf<Pair<LocalDate, List<LocalTime>>>()
    val ultimo = oggi.plusDays(giorniTotali.toLong())


    db.collection("appuntamenti")
        .get()
        .addOnSuccessListener { giorni ->
            for (giorno in giorni) {
                val dati = giorno.data
                val giornoCorrente =
                    LocalDate.parse(giorno.id, DateTimeFormatter.ofPattern("dd-mm-yyyy"))

                if (giornoCorrente.isEqual(oggi) ||
                    (giornoCorrente.isAfter(oggi) && giornoCorrente.isEqual(ultimo)) ||
                    giornoCorrente.isEqual(ultimo)
                ) {
                    val slotOrari = mutableListOf<LocalTime>()
                    for (key in dati.keys) {
                        slotOrari.add(
                            LocalTime.parse(
                                key.toString(),
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                        )

                    }
                    listaOccupati.add(giornoCorrente to slotOrari)
                }


            }

        }


    return listaOccupati
}

 */


@Preview
@Composable
fun Data() {

    val listaGiorniViewModel : ListaGiorniViewModel = viewModel()

    val listaGiorni by listaGiorniViewModel.listaGiorniAggiornata.collectAsState()

    var indexGiornoSelezionato by rememberSaveable {
        mutableIntStateOf(0)
    }

    var indexOrarioSelezionato by rememberSaveable {
        mutableIntStateOf(0)
    }

    val listState = rememberLazyListState()

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

                    }
                )

            }
        }

        Spacer(Modifier.height(30.dp))

        Box(modifier = Modifier
            .padding(horizontal = 8.dp)
            .weight(1f)){

            Box(modifier = Modifier
                .fillMaxSize(1f)
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
                }else{
                    LazyColumn(
                        contentPadding = PaddingValues(25.dp),
                        verticalArrangement = Arrangement.spacedBy(23.dp),
                        state = listState
                    ) {
                        items(orariDisponibili.size) { index ->
                            CardOrario(
                                orario = orariDisponibili[index],
                                index = index,
                                isSelected = index == indexOrarioSelezionato,
                                onCardSelected = { selectedIndex ->
                                    indexOrarioSelezionato = selectedIndex
                                }
                            )

                        }
                    }
                }



            }


        }
        
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            Log.d("Prova", listaGiorni[5].second.size.toString())
            Log.d("Prova", listaGiorni[5].first.toString())
            Log.d("Prova", listaGiorni[6].second.size.toString())
            Log.d("Prova", listaGiorni[7].second.size.toString())
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp, start = 14.dp, end = 14.dp),
            shape = RoundedCornerShape(15.dp),
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
fun CardOrario(
    orario: Pair<LocalTime, LocalTime>,
    index: Int,
    isSelected: Boolean,
    onCardSelected: (Int) -> Unit
) {


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