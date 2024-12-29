package com.example.marinobarbersalon.Admin.ProdottiPrenotati

import ProdottiPrenotatiVM
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Cliente.Screen
import com.example.marinobarbersalon.Cliente.Shopping.Prodotto
import com.example.marinobarbersalon.Cliente.Shopping.ProdottoPrenotato
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow

@Composable
fun VisualizzaProdottiPrenotati(
    prodottiPrenotatiVM: ProdottiPrenotatiVM = viewModel()
) {
    val listaProdottiPrenotati = prodottiPrenotatiVM.listaProdottiPrenotati.collectAsState().value
    val isLoading = prodottiPrenotatiVM.isLoadingProdotti.collectAsState().value

    // Effettua il fetch dei prodotti prenotati al primo rendering
    LaunchedEffect(Unit) {
        prodottiPrenotatiVM.fetchProdottiPrenotatiAttesa()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Titolo
        Text(
            text = "Prodotti Prenotati in Attesa",
            fontFamily = myFont,
            fontSize = 27.sp,
            color = my_white,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            // Mostra un indicatore di caricamento
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = my_gold,
                    modifier = Modifier.size(50.dp)
                )
            }
        } else if (listaProdottiPrenotati.isEmpty()) {
            // Mostra un messaggio quando non ci sono prodotti
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nessun prodotto in attesa.",
                    fontFamily = myFont,
                    fontSize = 20.sp,
                    color = my_white
                )
            }
        } else {
            // Mostra la lista dei prodotti prenotati
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(listaProdottiPrenotati) { (prodottoPrenotato, prodotto) ->
                    ProdottoPrenotatoCard(
                        prodottoPrenotato = prodottoPrenotato,
                        prodotto = prodotto,
                        onConferma = {
                            prodottiPrenotatiVM.confermaProdotto(prodottoPrenotato)
                        }
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
    onConferma: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = my_yellow),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Nome e Prezzo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = prodotto.nome,
                    fontFamily = myFont,
                    fontSize = 18.sp,
                    color = my_white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${String.format("%.2f", prodotto.prezzo)} €",
                    fontFamily = myFont,
                    fontSize = 16.sp,
                    color = my_white
                )
            }

            // Quantità e bottone "Conferma"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantità: ${prodottoPrenotato.quantita}",
                    fontFamily = myFont,
                    fontSize = 16.sp,
                    color = my_white
                )

                Button(
                    onClick = onConferma,
                    colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Conferma",
                        fontFamily = myFont,
                        fontSize = 16.sp,
                        color = my_white
                    )
                }
            }
        }
    }
}