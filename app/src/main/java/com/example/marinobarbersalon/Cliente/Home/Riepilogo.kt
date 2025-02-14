package com.example.marinobarbersalon.Cliente.Home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Clear

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Riepilogo(
    modifier: Modifier,
    userViewModel: UserViewModel,
    listaServiziViewModel: ListaServiziViewModel,
    idSer : String,
    orarioInizio : String,
    orarioFine : String,
    dataSelezionata : String,
    onSuccess : () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        var showDialogSuccess by rememberSaveable {
            mutableStateOf(false)
        }

        var showDialogError by rememberSaveable {
            mutableStateOf(false)
        }


        val listaServiziViewModel = listaServiziViewModel
        val listaServizi by listaServiziViewModel.listaServizi.collectAsState()
        val servizio = listaServizi.find { serv->
            serv.nome == idSer
        }

        Log.d("Servizio", dataSelezionata)

        //faccio il cast per formattarlo nello standard italiano
        val dataSelezionata = LocalDate.parse(dataSelezionata).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))


        if (!showDialogError && !showDialogSuccess){
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
                    Button(
                        onClick = {
                            userViewModel.aggiungiApp(
                                servizio = idSer,
                                orarioFine = orarioFine,
                                orarioInizio = orarioInizio,
                                dataSel = dataSelezionata.toString(),
                                onSuccess = {
                                    showDialogSuccess = true

                                    //Calcola il ritardo fino alla mattina dell'appuntamento per la notifica


                                    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    val parsedDate = inputFormat.parse(dataSelezionata)
                                    val formattedDate = parsedDate?.let { outputFormat.format(it) }

                                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                    val appointmentDate = sdf.parse("$formattedDate 08:00") ?: throw IllegalArgumentException("Data non valida")
                                    val delay = appointmentDate.time - System.currentTimeMillis()

                                    val oggi = outputFormat.format(System.currentTimeMillis())


                                    Log.d("Worker", "DELAY: $delay")
                                    Log.d("Worker", "Adesso inizio il work")

                                    //se la prenotazione coincide con la data odierna invio notifica istantanea
                                    if (formattedDate == oggi){
                                        userViewModel.scheduleNotificationWithWorker(
                                            context = context,
                                            titolo = "Promemoria Appuntamento",
                                            messaggio = "Hai un appuntamento oggi alle $orarioInizio per il servizio ${servizio.nome}.",
                                            delayInMillis = 0
                                        )

                                        Log.d("Worker", "Inviato adesso")

                                    } else if (delay > 0) {
                                        //altrimenti programmo la notifica con il Worker
                                        userViewModel.scheduleNotificationWithWorker(
                                            context = context,
                                            titolo = "Promemoria Appuntamento",
                                            messaggio = "Hai un appuntamento oggi alle $orarioInizio per il servizio ${servizio.nome}.",
                                            delayInMillis = delay
                                        )
                                        Log.d("Worker", "Work finito")
                                    }

                                },
                                onFailed = {
                                    showDialogError = true
                                }
                            )
                        },
                        enabled = !showDialogError && !showDialogSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 9.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = my_bordeaux,
                            disabledContainerColor = my_bordeaux
                        )
                    ) {
                        Text(text = "PRENOTA", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
                    }
                }


            }
        }else if (showDialogSuccess){
            DialogSuccesso(onDismiss = onSuccess)
        }else{
            DialogErrore(onDismiss = onSuccess )
        }
    }
}



@Composable
fun DialogSuccesso(onDismiss : () -> Unit ){


    Dialog(onDismissRequest = {
                         onDismiss()
    },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = true
        )
    ) {

        Column(modifier = Modifier
            .size(width = 300.dp, height = 230.dp)
            .background(color = Color.White, RoundedCornerShape(15.dp))
            .border(3.dp, my_bordeaux, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Icon(painter = painterResource(id = R.drawable.select_check_box_24dp_faf9f6_fill0_wght100_grad0_opsz24), contentDescription = "check",
                modifier = Modifier.size(80.dp),
                tint = Color.Black)

            Text(text = "Prenotazione effettuata \ncon successo",
                fontFamily = myFont,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center)
        }


    }

    
}


@Composable
fun DialogErrore(onDismiss : () -> Unit ){


    Dialog(onDismissRequest = {
        onDismiss()
    },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = true
        )
    ) {

        Column(modifier = Modifier
            .size(width = 300.dp, height = 230.dp)
            .background(color = Color.White, RoundedCornerShape(15.dp))
            .border(3.dp, my_bordeaux, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Sharp.Clear, contentDescription = "check",
                modifier = Modifier.size(80.dp),
                tint = Color.Black)

            Text(text = "Errore, lo slot è già\nstato occupato",
                fontFamily = myFont,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center)
        }


    }


}