package com.example.marinobarbersalon.Cliente.Shopping

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.marinobarbersalon.Cliente.Account.CardRecensione
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun Shop(
    modifier: Modifier,
    title : String,
    onNavigateToProdottoShop: (String) -> Unit
) {

    val listaProdottiViewModel : ListaProdottiViewModel = viewModel()
    listaProdottiViewModel.caricaListaProdotti(title)
    val listaProdotti by listaProdottiViewModel.listaProdotti.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Text(
            text = "Scegli un prodotto per $title",
            fontFamily = myFont,
            fontSize = 23.sp,
            color = my_white
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(listaProdotti) { prodotto ->
                Log.d("Image URI", prodotto.immagine)
                GridItem(prodotto, onNavigateToProdottoShop)
            }
            
        }

        /*
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                // Fai qualcosa con l'URI
                Log.d("Image URI", "Selected URI: $it")
            }
        }

// Bottone per avviare il selettore immagini
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleziona immagine")
        }

         */
        fd


         */


    }
}


@SuppressLint("DefaultLocale")
@Composable
fun GridItem(prodotto: Prodotto, onNavigateToProdottoShop: (String) -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        AsyncImage(
            model = Uri.parse(prodotto.immagine),
            contentDescription = "Immagine prodotto",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .border(2.dp, my_gold, RoundedCornerShape(10.dp))
                .clickable {
                    onNavigateToProdottoShop(prodotto.nome)
                },
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Text(
                text = String.format("%.2f", prodotto.prezzo)+"€",
                fontFamily = myFont,
                color = my_white,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Visible, // Il prezzo non sarà troncato
                modifier = Modifier.padding(start = 6.dp, bottom = 4.dp) // Spazio per separarlo dal nome
            )
        }

    }

}