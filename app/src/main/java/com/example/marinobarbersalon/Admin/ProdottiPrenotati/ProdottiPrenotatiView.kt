package com.example.marinobarbersalon.Admin.ProdottiPrenotati

import ProdottiPrenotatiVM
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.Cliente.Shopping.Prodotto
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoPrenotato
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.marinobarbersalon.ui.theme.my_green


@Composable
fun VisualizzaProdottiPrenotati(
    prodottiPrenotatiVM: ProdottiPrenotatiVM = viewModel()
) {
    val prodottiPrenotati = prodottiPrenotatiVM.listaProdottiPrenotati.collectAsState().value
    val isLoading = prodottiPrenotatiVM.isLoadingProdotti.collectAsState().value

    LaunchedEffect(Unit) {
        prodottiPrenotatiVM.fetchProdottiPrenotatiAttesa()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .padding(top = 64.dp)
    ) {
        //Titolo
        Text(
            text = "Prodotti Prenotati in Attesa",
            fontFamily = myFont,
            fontSize = 23.sp,
            color = my_white,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        if (isLoading) {
            //Caricamento
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = my_gold,
                    modifier = Modifier.size(100.dp)
                )
            }
        } else if (prodottiPrenotati.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nessun prodotto prenotato in attesa.",
                    fontFamily = myFont,
                    fontSize = 20.sp,
                    color = my_white
                )
            }
        } else {
            //Lista prodotti trovati
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(prodottiPrenotati) { (prodottoPrenotato, prodotto) ->
                    ProdottoPrenotatoCard(
                        prodottoPrenotato = prodottoPrenotato,
                        prodotto = prodotto,
                        prodottiPrenotatiVM = prodottiPrenotatiVM
                    )
                }
            }
        }
    }
}

@Composable
fun ProdottoPrenotatoCard(
    prodottoPrenotato: ProdottoPrenotato,
    prodotto: Prodotto,
    prodottiPrenotatiVM: ProdottiPrenotatiVM
) {
    var nomeCliente by remember { mutableStateOf("") }
    var cognomeCliente by remember { mutableStateOf("") }

    //Recupera i dettagli del cliente prima di fare qualunque cosa
    LaunchedEffect(prodottoPrenotato.utente) {
        if (prodottoPrenotato.utente != null) {
            prodottiPrenotatiVM.fetchClienteDetails(
                utenteReference = prodottoPrenotato.utente,
                onSuccess = { nome, cognome ->
                    nomeCliente = nome
                    cognomeCliente = cognome
                },
                onError = {
                    nomeCliente = "Non disponibile"
                    cognomeCliente = ""
                }
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(2.dp, my_gold, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = my_yellow),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //Immagine
            SubcomposeAsyncImage(
                model = Uri.parse(prodotto.immagine),
                contentDescription = "Immagine prodotto",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, my_gold, RoundedCornerShape(12.dp)),
                loading = {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, my_gold, RoundedCornerShape(12.dp))
                            .background(color = my_grey, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = my_gold,
                            strokeWidth = 2.dp
                        )
                    }
                },
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //Nome
                Text(
                    text = prodotto.nome,
                    fontFamily = myFont,
                    fontSize = 18.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                //Quantità
                Text(
                    text = "Quantità: ${prodottoPrenotato.quantita}",
                    fontFamily = myFont,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                //Prezzo
                Text(
                    text = "Prezzo: ${String.format("%.2f", prodotto.prezzo)} €",
                    fontFamily = myFont,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                //Nome e Cognome cliente
                Text(
                    text = "Cliente: $nomeCliente $cognomeCliente",
                    fontFamily = myFont,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

//            // Bottone conferma
//            Button(
//                onClick = {
//                    prodottiPrenotatiVM.confermaProdotto(prodottoPrenotato)
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux),
//                shape = RoundedCornerShape(8.dp),
//                modifier = Modifier.align(Alignment.CenterVertically)
//            ) {
//                Text(
//                    text = "Conferma",
//                    fontFamily = myFont,
//                    fontSize = 16.sp,
//                    color = my_white
//                )
//            }

            //Icona conferma
            IconButton(
                onClick = {
                    prodottiPrenotatiVM.confermaProdotto(prodottoPrenotato)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Conferma prodotto",
                    tint = my_green, // Colore dell'icona
                    modifier = Modifier.size(32.dp)
                )
            }

        }
    }
}


