package com.example.marinobarbersalon.Admin.VisualizzaProdotti

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
*               mettere apposto la grafica delle card;
*        second page:
*               mettere apposto la grafica delle card;
*               pulsante di "+" quantità (si sovrappone se nome lungo);
*               mettere pulsante per diminuire la quantità;
*               passare alla pagina aggiungi prodotto la categoria su cui siamo gia;
*        third page:
*           modificare tipo di img (chiedere a gianluca se è ok come voglio fare io);
*           accettare in input la categoria e quindi impostarla automaticamente
*                           (fatto gia in VisualizzaProdottiDettaglio);
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
            .padding(16.dp)
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Seleziona una categoria",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //Card per "Capelli"
        CategoriaCard(
            categoria = "capelli",
            isSelected = categoriaSelezionata == "capelli",
            onClick = {
                prodottiViewModel.onCategoriaSelezionata("capelli")
                onNavigateToNextPage("capelli")
            },
            idIcon = R.drawable.capelli_icona
        )

        //Card per "Barba"
        CategoriaCard(
            categoria = "barba",
            isSelected = categoriaSelezionata == "barba",
            onClick = {
                prodottiViewModel.onCategoriaSelezionata("barba")
                onNavigateToNextPage("barba")
            },
            idIcon = R.drawable.barba_icona
        )

        //Card per "Viso"
        CategoriaCard(
            categoria = "viso",
            isSelected = categoriaSelezionata == "viso",
            onClick = {
                prodottiViewModel.onCategoriaSelezionata("viso")
                onNavigateToNextPage("viso")
            },
            idIcon = R.drawable.viso_icona
        )
    }
}

@Composable
fun CategoriaCard(
    categoria: String,
    isSelected: Boolean,
    idIcon: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, my_gold, RoundedCornerShape(17.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(17.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = my_yellow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = idIcon),
                contentDescription = categoria,
                modifier = Modifier.size(48.dp),
                tint = if (isSelected) Color.White else Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = categoria,
                style = MaterialTheme.typography.headlineLarge,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}

//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
// PER LA SECONDA PAGINA


@Composable
fun VisualizzaProdottiDettaglio(
    categoria: String, //Categoria passata dalla navigazione
    prodottiViewModel: VisualizzaProdottiVM = viewModel(),
    onNavigateToAddProdotto: () -> Unit
) {
    val categoriaSelezionata = categoria
    val prodotti = prodottiViewModel.prodottiState.collectAsState().value


    Log.d("VisualizzaProdottiDettaglio", "Prodotti visualizzati: $prodotti")

    LaunchedEffect(Unit) {
        prodottiViewModel.fetchProdottiPerCategoria(categoriaSelezionata)
    }

    Log.d("VisualizzaProdottiDettaglio", "Prodotti visualizzati dopo launched effect: $prodotti")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Categoria: $categoriaSelezionata",
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn {
            items(prodotti) { prodotto ->
                ProdottoCard(prodotto = prodotto)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))


        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {onNavigateToAddProdotto()},
                colors = ButtonDefaults.buttonColors(containerColor = my_bordeaux)
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
    prodottiViewModel: VisualizzaProdottiVM = viewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val painter = rememberAsyncImagePainter(prodotto.immagine)
            Image(
                painter = painter,
                contentDescription = prodotto.nome,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = prodotto.nome,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Prezzo: ${prodotto.prezzo} €"
                )
                Text(
                    text = "Quantità: ${prodotto.quantita}"
                )

            }
            IconButton(
                onClick = {prodottiViewModel.increaseStock(prodotto)},
                modifier = Modifier.padding(start = 8.dp)
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
    onAggiungiSuccess: () -> Unit,
    onAnnullaClick: () -> Unit
) {
    val nome = aggiungiProdottoViewModel.nome.collectAsState().value
    val descrizione = aggiungiProdottoViewModel.descrizione.collectAsState().value
    val prezzo = aggiungiProdottoViewModel.prezzo.collectAsState().value
    val categoria = aggiungiProdottoViewModel.categoria.collectAsState().value
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
                    )
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
                    value = categoria,
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
                    )
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












