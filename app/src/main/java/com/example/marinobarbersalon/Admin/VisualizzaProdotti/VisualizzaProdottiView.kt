package com.example.marinobarbersalon.Admin.VisualizzaProdotti

import android.net.Uri
import android.text.Layout
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.toUpperCase
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.style.TextOverflow


/*
    first page:
        3 card cliccabili: Capelli, Barba e Viso
    second page:
        in base a quale card abbiamo cliccato carichiamo i dettagli
        di tutti i prodotti gia esistenti nel sistema di quella determinata categoria
        e abbiamo per ogni prodotto:
                            1) riquadro con foto a sinistra
                            2) nome e prezzo a destra
                            3) sotto a ciò il numero di scorte rimanenti e un bottone col "+" per aumentarle
                            4) sotto ancora tasto "aggiungi prodotto"

    third page:
        impostazione come la pagina precedente però con i vari campi che in questo caso sono di input
        per caricare un nuovo prodotto


*/




/*
*       TODO
*        first page:
*               mettere apposto la grafica delle card; OK
*        second page:
*               mettere apposto la grafica delle card;
*               pulsante di "+" quantità (si sovrappone se nome lungo);
*               mettere pulsante per diminuire la quantità;
*               passare alla pagina aggiungi prodotto la categoria su cui siamo gia;
*               mettere lo stesso spacio tra ultimo elemento della lazycolumn come in Vis. Servizi;
*        third page:
*           modificare tipo di img (chiedere a gianluca se è ok come voglio fare io);
*           accettare in input la categoria e quindi impostarla automaticamente
*                           (fatto gia in VisualizzaProdottiDettaglio); OK
*
**/




//--------------------------------------------------------------------------------------------------
//PRIMA PAGINA

@Composable
fun VisualizzaProdotti(
    prodottiViewModel: VisualizzaProdottiVM = viewModel(),
    onNavigateToNextPage: (String) -> Unit
) {
    val categoriaSelezionata = prodottiViewModel.categoriaSelezionata.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Seleziona una categoria:",
            fontFamily = myFont,
            fontSize = 27.sp,
            color = my_white
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                            .background(color = my_white, shape = RoundedCornerShape(25.dp))
                            .aspectRatio(1f)
                            .clickable {
                                prodottiViewModel.onCategoriaSelezionata("capelli")
                                onNavigateToNextPage("capelli")
                            }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.capelli_icona),
                            contentDescription = "Icona capelli",
                            modifier = Modifier.padding(30.dp)
                        )
                    }

                    Text(
                        text = "CAPELLI",
                        fontSize = 25.sp,
                        fontFamily = myFont,
                        color = my_white
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                            .background(color = my_white, shape = RoundedCornerShape(25.dp))
                            .aspectRatio(1f)
                            .clickable {
                                prodottiViewModel.onCategoriaSelezionata("barba")
                                onNavigateToNextPage("barba")
                            }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.barba_icona),
                            contentDescription = "Icona barba",
                            modifier = Modifier.padding(30.dp)
                        )
                    }

                    Text(
                        text = "BARBA",
                        fontSize = 25.sp,
                        fontFamily = myFont,
                        color = my_white
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                        .background(color = my_white, shape = RoundedCornerShape(25.dp))
                        .fillMaxWidth(0.5f)
                        .clickable {
                            prodottiViewModel.onCategoriaSelezionata("viso")
                            onNavigateToNextPage("viso")
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.viso),
                        contentDescription = "Icona viso",
                        modifier = Modifier.padding(top = 30.dp, bottom = 30.dp, start = 30.dp, end = 20.dp)
                    )
                }

                Text(
                    text = "VISO",
                    fontSize = 25.sp,
                    fontFamily = myFont,
                    color = my_white
                )
            }
        }
    }
}



//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
// PER LA SECONDA PAGINA


@Composable
fun VisualizzaProdottiDettaglio(
    categoria: String,
    prodottiViewModel: VisualizzaProdottiVM = viewModel(),
    onNavigateToAddProdotto: (String) -> Unit
) {
    val categoriaSelezionata = categoria
    val prodotti = prodottiViewModel.prodottiState.collectAsState().value

    LaunchedEffect(Unit) {
        prodottiViewModel.fetchProdottiPerCategoria(categoriaSelezionata)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .padding(top = 64.dp)
    ) {
        Text(
            text = "Categoria: $categoriaSelezionata",
            fontFamily = myFont,
            fontSize = 23.sp,
            color = my_white,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 25.dp,
                end = 12.dp,
                bottom = 25.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.weight(1f) //togliere sovrapposizione
        ) {
            items(prodotti) { prodotto ->
                ProdottoCard(prodotto, prodottiViewModel)
            }
        }

        //Bottone "Aggiungi Prodotto"
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Button(
                onClick = { onNavigateToAddProdotto(categoria) },
//                modifier = Modifier
//                    .height(48.dp)
//                    .fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux),
//                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Aggiungi Prodotto",
                    fontFamily = myFont,
                    fontSize = 25.sp,
                    color = my_gold
                )
            }
        }
    }
}

@Composable
fun ProdottoCard(
    prodotto: Prodotto,
    prodottiViewModel: VisualizzaProdottiVM
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //Immagine del prodotto
        SubcomposeAsyncImage(
            model = Uri.parse(prodotto.immagine),
            contentDescription = "Immagine prodotto",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .border(2.dp, my_gold, RoundedCornerShape(10.dp)),
            loading = {
                Box(
                    Modifier.size(180.dp)
                        .border(2.dp, my_gold, RoundedCornerShape(10.dp))
                        .background(color = my_grey, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(10.dp),
                        color = my_gold,
                        strokeWidth = 5.dp
                    )
                }
            },
            contentScale = ContentScale.Crop,
            colorFilter = if (prodotto.quantita == 0) {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            } else {
                null
            }
        )

        //Dettagli del prodotto
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Nome
            Text(
                text = prodotto.nome,
                fontFamily = myFont,
                color = my_white,
                fontSize = 18.sp,
                maxLines = 2,
                lineHeight = 14.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            //Prezzo
            Text(
                text = String.format("%.2f", prodotto.prezzo) + "€",
                fontFamily = myFont,
                color = my_white,
                fontSize = 18.sp,
                maxLines = 1,
                modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Quantita
            Text(
                text = "Quantità: ${prodotto.quantita}",
                fontFamily = myFont,
                color = my_white,
                fontSize = 16.sp
            )
            //Aumenta quantita
            IconButton(
                onClick = { prodottiViewModel.increaseStock(prodotto) }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Aggiungi prodotto",
                    tint = my_bordeaux
                )
            }
        }
    }
}



//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
// PER LA TERZA PAGINA
@Composable
fun AggiungiProdotto(
    aggiungiProdottoViewModel: VisualizzaProdottiVM = viewModel(),
    categoria : String,
    onAggiungiSuccess: () -> Unit,
    onAnnullaClick: () -> Unit
) {
    val nome = aggiungiProdottoViewModel.nome.collectAsState().value
    val descrizione = aggiungiProdottoViewModel.descrizione.collectAsState().value
    val prezzo = aggiungiProdottoViewModel.prezzo.collectAsState().value
    val categoriaVM = aggiungiProdottoViewModel.categoria.collectAsState().value
    val quantita = aggiungiProdottoViewModel.quantita.collectAsState().value
    val immagine = aggiungiProdottoViewModel.immagine.collectAsState().value


    //Form validation
    val formErrors = aggiungiProdottoViewModel.formErrors.collectAsState().value
    val showErrorDialog = remember { mutableStateOf(false) }
    val isFormSubmitted = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        aggiungiProdottoViewModel.validateForm()
        if (formErrors.isNotEmpty()) {
            showErrorDialog.value = true
        }
        aggiungiProdottoViewModel.setCategoria(categoria)

    }

    if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = {
                Text(
                    text = "Errore di validazione",
                    fontFamily = myFont,
                    fontSize = 20.sp
                )
            },
            text = {
                Column {
                    formErrors.forEach { error ->
                        Text(error, color = my_bordeaux, fontFamily = myFont, fontSize = 19.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog.value = false
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, my_gold, RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = my_yellow)
                ) {
                    Text("OK", fontFamily = myFont, fontSize = 18.sp, color = my_gold)
                }
            },
            shape = RoundedCornerShape(17.dp),
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
            containerColor = my_yellow
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aggiungi Prodotto",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = my_white,
            fontFamily = myFont
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = my_yellow),
            shape = RoundedCornerShape(17.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                //Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = { aggiungiProdottoViewModel.onNomeChange(it) },
                    label = { Text("Nome Prodotto") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,           // Colore del bordo selezionato
                        focusedLabelColor = Color.Black,      // Colore della label quando il campo è selezionato
                        cursorColor = my_bordeaux,             // Colore del cursore
                        unfocusedBorderColor = Color.Black,       // Colore del bordo non selezionato
                        unfocusedLabelColor = Color.Black         // Colore della label non selezionata
                    ),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descrizione
                OutlinedTextField(
                    value = descrizione,
                    onValueChange = { aggiungiProdottoViewModel.onDescrizioneChange(it) },
                    label = { Text("Descrizione") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Categoria
                OutlinedTextField(
                    value = categoriaVM,
                    onValueChange = { aggiungiProdottoViewModel.onCategoriaChange(it) },
                    label = { Text("Categoria") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    ),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Prezzo
                OutlinedTextField(
                    value = if (prezzo == 0.0) "" else prezzo.toString(),
                    onValueChange = { newValue ->
                        val cleanedValue = newValue.replace(',', '.')
                        val validValue = cleanedValue.toDoubleOrNull()
                        if (validValue != null) {
                            aggiungiProdottoViewModel.onPrezzoChange(validValue)
                        }
                    },
                    label = { Text("Prezzo (€)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Quantita
                OutlinedTextField(
                    value = if (quantita == 0) "" else quantita.toString(),
                    onValueChange = { newValue ->
                        val validInput = newValue.toIntOrNull() != null || newValue.isEmpty()

                        if (validInput) {
                            aggiungiProdottoViewModel.onQuantitaChange(newValue.toIntOrNull() ?: 0)
                        }
                    },
                    label = { Text("Quantita") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Immagine
                OutlinedTextField(
                    value = immagine,
                    onValueChange = { aggiungiProdottoViewModel.onImmagineChange(it) },
                    label = { Text("Immagine") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = myFont, fontSize = 25.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    isFormSubmitted.value = true
                    aggiungiProdottoViewModel.validateForm()
                    if (formErrors.isEmpty()) {
                        aggiungiProdottoViewModel.aggiungiProdotto(
                            onSuccess = onAggiungiSuccess,
                            onError = { /* Mostra errore */ }
                        )
                    } else {
                        showErrorDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)
            ) {
                Text(
                    text = "Aggiungi",
                    fontFamily = myFont,
                    fontSize = 25.sp,
                    color = my_gold
                )
            }

            Button(
                onClick = {
                    aggiungiProdottoViewModel.resetFields()
                    isFormSubmitted.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)
            ) {
                Text(
                    text = "Annulla",
                    fontFamily = myFont,
                    fontSize = 25.sp,
                    color = my_white
                )
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------












