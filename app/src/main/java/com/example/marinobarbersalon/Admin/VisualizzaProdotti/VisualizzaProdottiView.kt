package com.example.marinobarbersalon.Admin.VisualizzaProdotti

import android.net.Uri
import android.text.Layout
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.marinobarbersalon.Admin.Servizi.DialogGenerico


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
*               mettere apposto la grafica delle card; OK
*               pulsante di "+" quantità (si sovrappone se nome lungo); OK
*               mettere pulsante per diminuire la quantità; NON BISOGNA FARLO
*               passare alla pagina aggiungi prodotto la categoria su cui siamo gia; OK
*               mettere lo stesso spacio tra ultimo elemento della lazycolumn come in Vis. Servizi; OK
*        third page:
*           modificare tipo di img (chiedere a gianluca se è ok come voglio fare io); CAPIRE MEGLIO COME FARE
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
                            .border(
                                width = 2.dp,
                                color = my_gold,
                                shape = RoundedCornerShape(25.dp)
                            )
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
                            .border(
                                width = 2.dp,
                                color = my_gold,
                                shape = RoundedCornerShape(25.dp)
                            )
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
    val context = LocalContext.current
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
                shape = RoundedCornerShape(10.dp)
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
                    Modifier
                        .size(180.dp)
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
                modifier = Modifier.weight(1f),

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
    categoria: String,
    onAggiungiSuccess: () -> Unit,
    onAnnullaClick: () -> Unit
) {
    val nome = aggiungiProdottoViewModel.nome.collectAsState().value
    val descrizione = aggiungiProdottoViewModel.descrizione.collectAsState().value
    val prezzo = aggiungiProdottoViewModel.prezzo.collectAsState().value
    val categoriaVM = aggiungiProdottoViewModel.categoria.collectAsState().value
    val quantita = aggiungiProdottoViewModel.quantita.collectAsState().value
//    val immagine = aggiungiProdottoViewModel.immagine.collectAsState().value

    //per le img su supabase
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    //Form val
    val formErrors = aggiungiProdottoViewModel.formErrors.collectAsState().value
    val showErrorDialog = remember { mutableStateOf(false) }
    val isFormSubmitted = remember { mutableStateOf(false) }

    //dialog succ o fail
    val showDialogSuccess = remember { mutableStateOf(false) }
    val showDialogError = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        aggiungiProdottoViewModel.validateForm()
        if (formErrors.isNotEmpty()) {
            showErrorDialog.value = true
        }
        aggiungiProdottoViewModel.setCategoria(categoria)
    }

    //Visualizza dialog di successo
    if (showDialogSuccess.value) {
        DialogGenerico(
            titolo = "Successo",
            messaggio = "Prodotto aggiunto con successo!",
            icona = painterResource(id = R.drawable.select_check_box_24dp_faf9f6_fill0_wght100_grad0_opsz24),
            onDismiss = {
                showDialogSuccess.value = false
                onAggiungiSuccess() // Naviga o esegui azione al completamento
            }
        )
    }

    //Visualizza dialog di errore
    if (showDialogError.value) {
        DialogGenerico(
            titolo = "Errore",
            messaggio = "Errore durante l'aggiunta del prodotto.",
            icona = rememberVectorPainter(Icons.Filled.Error),
            onDismiss = { showDialogError.value = false }
        )
    }

    //Visualizza dialog form invalid
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
                        .border(
                            width = 2.dp,
                            color = my_gold,
                            shape = RoundedCornerShape(17.dp)
                        )
                        .background(my_bordeaux, shape = RoundedCornerShape(17.dp)) //Sfondo!!!
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = my_white
                    ),
                    shape = RoundedCornerShape(17.dp)
                ) {
                    Text(
                        text = "OK",
                        fontFamily = myFont,
                        fontSize = 20.sp,
                        color = my_white
                    )
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
            .padding(32.dp)
            .verticalScroll(rememberScrollState()), //scroll
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Titolo
        Text(
            text = "Aggiungi Prodotto",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(top = 64.dp)
                .fillMaxWidth(), //Titolo fisso
            color = my_white,
            fontFamily = myFont,
            textAlign = TextAlign.Center,

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
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Descrizione
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
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Categoria
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

                //Prezzo
                OutlinedTextField(
                    value = if (prezzo == 0.0) ""  else String.format("%.2f", prezzo),
                    onValueChange = { newValue ->
                        val cleanedValue = newValue.replace(',', '.') //Sost virgola con punto (nelle tastiere numeric c'è la virgola in italia)
                        val validValue = cleanedValue.toDoubleOrNull()

                        // Controlla se il valore è valido e all'interno del range consentito
                        if (validValue != null && validValue <= 999999.99) {
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
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = my_bordeaux,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Selettore immagine
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    uri?.let {
                        selectedImageUri.value = it
                    }
                }

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux),
                ) {
                    Text(
                        text = "Seleziona Immagine",
                        fontFamily = myFont,
                        fontSize = 25.sp,
                        color = my_white
                    )
                }

                //Mostra anteprima immagine
                selectedImageUri.value?.let { uri ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Anteprima immagine",
                        modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, my_bordeaux, RoundedCornerShape(10.dp)),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Pulsanti Aggiungi e Annulla
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    selectedImageUri.value?.let { uri ->
                        Log.d("aggiungiProdotto", "URI immagine selezionato: $uri")
                        aggiungiProdottoViewModel.aggiungiProdottoWithImage(
                            context = context,
                            imageUri = uri.toString(),
                            onSuccess = {
                                Log.d("aggiungiProdotto", "Prodotto aggiunto con successo")
                                showDialogSuccess.value = true
                            },
                            onError = { e ->
                                Log.e("aggiungiProdotto", "Errore durante l'aggiunta del prodotto: ${e.message}", e)
                                showDialogError.value = true
                            }
                        )
                    } ?: run {
                        Log.e("aggiungiProdotto", "Nessuna immagine selezionata")
                        showErrorDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux),
                shape = RoundedCornerShape(10.dp)
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
                    selectedImageUri.value = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux),
                shape = RoundedCornerShape(10.dp)
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












