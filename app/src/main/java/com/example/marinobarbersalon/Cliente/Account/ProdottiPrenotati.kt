package com.example.marinobarbersalon.Cliente.Account

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon

import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.marinobarbersalon.Cliente.Home.UserViewModel
import com.example.marinobarbersalon.Cliente.Shopping.Prodotto
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoPrenotato
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow

@Composable
fun ProdottiPrenotati(modifier : Modifier, userViewModel: UserViewModel) {

    userViewModel.caricaProdottiPrenotati()

    val listaProdottiPrenotati by userViewModel.listaProdottiPrenotati.collectAsState()

    //Log.d("Ricaricato", listaProdottiPrenotati.size.toString())

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),

    ) {

        Text(
            text = "Da ritirare in negozio:",
            fontSize = 25.sp,
            fontFamily = myFont,
            color = my_white,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )

        if (listaProdottiPrenotati.isNotEmpty()){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(listaProdottiPrenotati) { item ->
                    ProdPrenItem(
                        item = item,
                        onDelete = {
                            userViewModel.annullaPrenotazioneProdotto(item.first)
                        }
                    )
                }
            }
        }else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.border(width = 3.dp, color = my_gold, shape = RoundedCornerShape(20.dp))
                        .padding(vertical = 20.dp),
                    text = "NON CI SONO PRODOTTI PRENOTATI",
                    color = my_white,
                    fontFamily = myFont,
                    fontSize = 30.sp,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center
                )
            }
        }





    }

}

@Composable
fun ProdPrenItem(item: Pair<ProdottoPrenotato, Prodotto>, onDelete : () -> Unit) {

    var contextMenuAnnulla by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    val haptics = LocalHapticFeedback.current


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(my_yellow)
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Immagine del prodotto
        SubcomposeAsyncImage(
            model = Uri.parse(item.second.immagine),
            contentDescription = "Immagine prodotto",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, my_gold, RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = my_gold,
                    strokeWidth = 4.dp
                )
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Dettagli del prodotto: Quantità e Prezzo
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.second.nome,
                fontSize = 19.sp,
                fontFamily = myFont,
                color = my_grey,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Quantità:\n${item.first.quantita}",
                    fontSize = 16.sp,
                    fontFamily = myFont,
                    color = my_grey,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Prezzo:\n${"%.2f".format(item.second.prezzo * item.first.quantita)}€",
                    fontSize = 16.sp,
                    fontFamily = myFont,
                    color = my_grey,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Bottone elimina
        Button(
            onClick = { contextMenuAnnulla = 1 },
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = my_bordeaux
            ),
            contentPadding = PaddingValues(0.dp), // Rimuove padding extra
            modifier = Modifier.size(75.dp) // Imposta una dimensione coerente
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Elimina prodotto",
                tint = Color.Black,
                modifier = Modifier.size(35.dp)
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = 2.dp,
        color = my_gold
    )

    if (contextMenuAnnulla != null) {
        AnnullaActionsSheet(
            onDismissSheet = { contextMenuAnnulla = null },
            annulla = {
                onDelete()
            },
            textSheet = "Vuoi annullare la prenotazione del prodotto?"
        )
    }

}