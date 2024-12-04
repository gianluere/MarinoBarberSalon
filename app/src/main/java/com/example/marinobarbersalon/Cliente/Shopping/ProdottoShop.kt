package com.example.marinobarbersalon.Cliente.Shopping

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun ProdottoShop(
    modifier: Modifier = Modifier,
    nomeProdotto: String
    ) {
    val listaProdottiViewModel : ListaProdottiViewModel = viewModel()
    listaProdottiViewModel.trovaProdotto(nomeProdotto)
    val prodotto by listaProdottiViewModel.prodotto.collectAsState()
    Log.d("Passaggio", prodotto.nome+ " " + prodotto.categoria)

    Column(
        modifier = modifier.fillMaxSize()
            .padding(12.dp)
    ) {
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
            AsyncImage(
                model = Uri.parse(prodotto.immagine),
                contentDescription = "Immagine prodotto",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(2.dp, my_gold, RoundedCornerShape(10.dp))
                    .fillMaxHeight(0.5f),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = prodotto.descrizione,
            fontSize = 24.sp,
            fontFamily = myFont,
            color = my_white
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Quantità: " + prodotto.quantita.toString(),
                fontSize = 24.sp,
                fontFamily = myFont,
                color = my_white,
                modifier = Modifier.padding(end = 25.dp)
            )

            Button(
                modifier = Modifier
                    .border(width = 4.dp, color = my_gold, shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_white
                ),
                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 20.dp)
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = "Icona remove",
                    //modifier = Modifier.size(15.dp),
                    tint = my_grey
                )

            }

            Button(
                modifier = Modifier
                    .border(width = 4.dp, color = my_gold, shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_white
                ),
                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 20.dp)
            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = "Icona remove",
                    //modifier = Modifier.size(15.dp),
                    tint = my_grey
                )

            }



        }

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 10.dp, start = 15.dp, end = 15.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            Button(
                onClick = {
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 30.dp)
            ) {
                Text(text = "PRENOTA", color = my_gold, fontFamily = myFont, fontSize = 25.sp)
            }
        }


    }


}