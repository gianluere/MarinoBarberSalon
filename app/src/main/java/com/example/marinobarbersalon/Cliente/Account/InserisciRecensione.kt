package com.example.marinobarbersalon.Cliente.Account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_yellow

@Composable
fun InserisciRecensione(
    modifier: Modifier = Modifier,
    listaRecensioniViewModel : ListaRecensioniViewModel,
    userViewModel: UserViewModel,
    onSuccess : () -> Unit
) {

    var descrizione by rememberSaveable {
        mutableStateOf("")
    }

    var rating by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }

    var showDialogSuccess by rememberSaveable {
        mutableStateOf(false)
    }

    val stellePiene = rating.toInt()
    val mezzaStella = if (rating - stellePiene >= 0.5) 1 else 0
    val stelleVuote = 5 - stellePiene - mezzaStella


    if (!showDialogSuccess){
        Column(
            modifier = modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween

        ){
            Column(
                modifier = Modifier.fillMaxWidth()
                    .height(200.dp)
                    .background(color = my_yellow, shape = RoundedCornerShape(20.dp))
                    .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(20.dp))
                    .padding(10.dp),
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
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(1f).weight(1f),
                    value = descrizione,
                    onValueChange = {
                        if (it.length <= 280){
                            descrizione = it
                        }
                    }
                    ,
                    placeholder = {
                        Text(
                            text = "Descrizione",
                            fontSize = 17.sp,
                            fontFamily = myFont,
                            color = my_grey,
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ){

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {

                        for (i in 1..5) {
                            // Calcola se mostrare la stella piena, mezza o vuota
                            val starRatingValue = i.toDouble()
                            val isFullStar = rating >= starRatingValue
                            val isHalfStar = rating in (starRatingValue - 0.5)..(starRatingValue - 0.1)

                            // Imposta l'icona e il colore della stella
                            Icon(
                                painter = when {
                                    isFullStar -> painterResource(R.drawable.baseline_star_24)
                                    isHalfStar -> painterResource(R.drawable.outline_star_half_24) // Si può sostituire con un'icona di mezza stella
                                    else -> painterResource(R.drawable.outline_star_border_24)
                                },
                                contentDescription = null,
                                tint = if (isFullStar || isHalfStar) my_gold else Color.Black,
                                modifier = Modifier
                                    .size(24.dp)
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


                    Text(
                        text = rating.toString(),
                        fontFamily = myFont,
                        fontSize = 20.sp,
                        color = my_grey,
                    )




                }

            }

            Button(
                onClick = {
                    listaRecensioniViewModel.inserisciRecensione(
                        Recensione(
                            nome = userViewModel.userState.value.nome + userViewModel.userState.value.cognome,
                            stelle = rating,
                            descrizione = descrizione
                        ),
                        onCompleted = {
                            showDialogSuccess = true
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                )
            ) {
                Text(text = "CONFERMA", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
            }

        }
    }else{
        DialogSuccessoRecensione(onDismiss = onSuccess)
    }




}




@Composable
fun DialogSuccessoRecensione(onDismiss : () -> Unit ){


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

            Text(text = "Recensione inviata \ncon successo",
                fontFamily = myFont,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center)
        }


    }


}