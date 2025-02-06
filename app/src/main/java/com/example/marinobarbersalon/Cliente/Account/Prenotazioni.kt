package com.example.marinobarbersalon.Cliente.Account

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun Prenotazioni(modifier: Modifier, userViewModel: UserViewModel) {
    val listaPrenotazioni by userViewModel.listaAppuntamenti.collectAsState()

    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Le tue prenotazioni:",
                color = my_white,
                fontSize = 25.sp,
                fontFamily = myFont,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )

            if (listaPrenotazioni.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(25.dp),
                    verticalArrangement = Arrangement.spacedBy(23.dp)
                ) {
                    items(listaPrenotazioni) { appuntamento ->
                        CardPrenotazione(
                            appuntamento = appuntamento,
                            annulla = {
                                loading = true
                                userViewModel.annullaPrenotazione(
                                    appuntamento = appuntamento,
                                    finito = {
                                        loading = false
                                    },
                                    errore = {
                                        loading = false
                                        Toast.makeText(context, "Errore di connessione", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .border(width = 3.dp, color = my_gold, shape = RoundedCornerShape(20.dp))
                            .padding(vertical = 20.dp),
                        text = "NON CI SONO PRENOTAZIONI",
                        color = my_white,
                        fontFamily = myFont,
                        fontSize = 30.sp,
                        lineHeight = 35.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = my_gold, modifier = Modifier.size(50.dp))
            }
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardPrenotazione(appuntamento: Appuntamento, annulla: () -> Unit) {
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

    var contextMenuAnnulla by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    val haptics = LocalHapticFeedback.current



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(2.dp, my_gold, RoundedCornerShape(17.dp))
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    if (attivo) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        contextMenuAnnulla = 1
                    }
                }
            ),
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
                    fontSize = 21.sp,
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
                    text = appuntamento.data,
                    fontFamily = myFont,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                Text(
                    text = appuntamento.orarioInizio + " - " + appuntamento.orarioFine,
                    fontFamily = myFont,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                Text(
                    text = String.format("%.2f", appuntamento.prezzo)+"€",
                    fontFamily = myFont,
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }

        }
    }

    if (contextMenuAnnulla != null) {
        AnnullaActionsSheet(
            onDismissSheet = { contextMenuAnnulla = null },
            annulla = {
                annulla()
            },
             textSheet = "Vuoi annullare la tua prenotazione?"
        )
    }




}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnullaActionsSheet(onDismissSheet: () -> Unit, annulla: () -> Unit, textSheet : String) {
    ModalBottomSheet(
        onDismissRequest = {onDismissSheet()},
        containerColor = my_grey
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = textSheet,
            textAlign = TextAlign.Center,
            color = my_white,
            fontFamily = myFont,
            fontSize = 24.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    onDismissSheet()
                },
                modifier = Modifier
                    .width(160.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                )
            ) {
                Text(text = "ANNULLA", color = my_gold, fontFamily = myFont, fontSize = 22.sp)
            }

            Button(
                onClick = {
                    annulla()
                    onDismissSheet()
                },
                modifier = Modifier
                    .width(180.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                )
            ) {
                Text(text = "CONFERMA", color = my_gold, fontFamily = myFont, fontSize = 22.sp)
            }
        }
    }
}