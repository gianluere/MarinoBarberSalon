package com.example.marinobarbersalon.Cliente.Shopping


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun SelezionaShop(modifier: Modifier) {

    Column(
        modifier = modifier
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
                            //.clip(shape = RoundedCornerShape(10.dp))
                            .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                            .background(color = my_white, shape = RoundedCornerShape(25.dp))
                            .aspectRatio(1f)

                    ){
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
                            //.clip(shape = RoundedCornerShape(10.dp))
                            .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                            .background(color = my_white, shape = RoundedCornerShape(25.dp))
                            .aspectRatio(1f)

                    ){
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
                        //.clip(shape = RoundedCornerShape(10.dp))
                        .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                        .background(color = my_white, shape = RoundedCornerShape(25.dp))
                        .fillMaxWidth(0.5f)

                ){
                    Image(
                        painter = painterResource(id = R.drawable.viso),
                        contentDescription = "Icona barba",
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