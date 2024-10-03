package com.example.marinobarbersalon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_yellow
import java.time.LocalDate
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


@Preview
@Composable
fun Data() {

    var indexSelezionato by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 30.dp, bottom = 30.dp)) {
        LazyRow(
            modifier = Modifier.background(color = Color(0xFFF5F5DC)),
            contentPadding = PaddingValues(25.dp),
            horizontalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            items(listaDate.size){index ->
                CardGiorno(
                    index = index,
                    isSelected = index == indexSelezionato,
                    onCardSelected = { selectedIndex ->
                        indexSelezionato = selectedIndex
                    }
                )

            }
        }

        Spacer(Modifier.height(30.dp))

        Box(modifier = Modifier
            .padding(horizontal = 8.dp)){

            Box(modifier = Modifier.fillMaxSize()
                .border(2.dp, my_gold, RoundedCornerShape(17.dp))
                .background(color = my_yellow, RoundedCornerShape(17.dp))){

                LazyColumn(
                    contentPadding = PaddingValues(25.dp),
                    verticalArrangement = Arrangement.spacedBy(23.dp)
                ) {
                    items(listaDate.size) { index ->
                        CardOrario(
                            index = index,
                            isSelected = index == indexSelezionato,
                            onCardSelected = { selectedIndex ->
                                indexSelezionato = selectedIndex
                            }
                        )

                    }
                }

            }
        }


    }


}


@Composable
fun CardGiorno(index : Int, isSelected: Boolean, onCardSelected: (Int) -> Unit) {
    val giorno = listaDate[index]
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
fun CardOrario(index: Int, isSelected: Boolean, onCardSelected: (Int) -> Unit) {

    val giorno = listaDate[index]
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
            Text(text = giorno.format(DateTimeFormatter.ofPattern("MMMM", Locale("it"))).replaceFirstChar { it.uppercase() },
                fontFamily = myFont,
                color = Color.Black,
                fontSize = 17.sp)
        }

    }

}