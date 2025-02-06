package com.example.marinobarbersalon.Cliente.Shopping

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.marinobarbersalon.Cliente.Home.DialogErrore
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun ProdottoShop(
    modifier: Modifier = Modifier,
    nomeProdotto: String,
    onSuccess: () -> Unit
) {
    val listaProdottiViewModel: ListaProdottiViewModel = viewModel()
    listaProdottiViewModel.trovaProdotto(nomeProdotto)
    val prodotto by listaProdottiViewModel.prodotto.collectAsState()

    var counter by remember { mutableIntStateOf(1) }
    var showDialogSuccess by rememberSaveable { mutableStateOf(false) }
    var showDialogError by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag("prenotaprodotto")
        ) {
            if (!showDialogError && !showDialogSuccess) {
                Text(
                    text = prodotto.nome,
                    fontSize = 27.sp,
                    fontFamily = myFont,
                    color = my_white
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = Uri.parse(prodotto.immagine),
                        contentDescription = "Immagine prodotto",
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(2.dp, my_gold, RoundedCornerShape(10.dp))
                            .fillMaxHeight(0.5f),
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = my_gold,
                                strokeWidth = 5.dp
                            )
                        },
                        error = {
                            Image(
                                painter = painterResource(R.drawable.placeholder),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                }

                Text(
                    text = prodotto.descrizione,
                    fontSize = 22.sp,
                    fontFamily = myFont,
                    color = my_white
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (prodotto.quantita > 0) {
                        Text(
                            text = "Quantità: $counter",
                            fontSize = 24.sp,
                            fontFamily = myFont,
                            color = my_white,
                            modifier = Modifier.padding(end = 25.dp)
                        )

                        Button(
                            modifier = Modifier.border(
                                width = 4.dp,
                                color = my_gold,
                                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                            ),
                            onClick = { if (counter > 1) counter -= 1 },
                            enabled = prodotto.quantita > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = my_white,
                                disabledContainerColor = Color(0xFF708090)
                            ),
                            shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                            contentPadding = PaddingValues(0.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 20.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = "Icona remove",
                                tint = my_grey
                            )
                        }

                        Button(
                            modifier = Modifier.border(
                                width = 4.dp,
                                color = my_gold,
                                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                            ),
                            onClick = { if (counter < prodotto.quantita) counter += 1 },
                            enabled = prodotto.quantita > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = my_white,
                                disabledContainerColor = Color(0xFF708090)
                            ),
                            shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                            contentPadding = PaddingValues(0.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 20.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Icona add",
                                tint = my_grey
                            )
                        }
                    } else {
                        Text(
                            text = "Attualmente non disponibile",
                            fontSize = 24.sp,
                            fontFamily = myFont,
                            color = my_white,
                            modifier = Modifier.padding(end = 25.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp, start = 15.dp, end = 15.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = {
                            isLoading = true
                            listaProdottiViewModel.prenotaProdotto(
                                prodotto,
                                counter,
                                onSuccess = {
                                    isLoading = false
                                    showDialogSuccess = true
                                },
                                onFailed = {
                                    isLoading = false
                                    showDialogError = true
                                }
                            )
                        },
                        enabled = prodotto.quantita > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("PRENOTA"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = my_bordeaux,
                            disabledContainerColor = Color(0xFF708090)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 30.dp)
                    ) {
                        Text(text = "PRENOTA", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
                    }
                }
            } else if (showDialogSuccess) {
                DialogSuccessoProdotto(onDismiss = onSuccess)
            } else {
                DialogErroreProdotto(onDismiss = onSuccess)
            }
        }

        // Overlay con CircularProgressIndicator quando isLoading è true
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = my_gold, modifier = Modifier.size(50.dp))
            }
        }
    }
}




@Composable
fun DialogSuccessoProdotto(onDismiss : () -> Unit ){


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

            Text(text = "Prodotto prenotato \ncon successo",
                fontFamily = myFont,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center)
        }


    }


}


@Composable
fun DialogErroreProdotto(onDismiss : () -> Unit ){


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

            Text(text = "Errore, verificare la\ndisponibilità del prodotto",
                fontFamily = myFont,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center)
        }


    }


}