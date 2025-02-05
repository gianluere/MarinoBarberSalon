package com.example.marinobarbersalon.Cliente.Home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont

import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white



@Composable
fun HomeScreen(
    modifier: Modifier,
    onNavigateToSelezionaServizioBarba: () -> Unit,
    onNavigateToSelezionaServizioCapelli: () -> Unit,
    userViewModel: UserViewModel
) {



    val userState by userViewModel.userState.collectAsState()

    /*
    LaunchedEffect(userState.state){
        when(userState.state){
            is AuthState.Unauthenticated -> onNavigateToLogin()
            else -> Unit
        }
    }

     */


    if (userState.nome.isNullOrEmpty() ) {
        // Mostra una schermata di caricamento o uno stato intermedio
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // Indicatore di caricamento
        }
    } else{
        Column(
            modifier.fillMaxSize()
        ) {

            Log.d("NAVBAR", userState.nome.toString())

            SelezionaTipo(userViewModel,
                onNavigateToSelezionaServizioBarba,
                onNavigateToSelezionaServizioCapelli)
        }
    }




}

@Composable
fun SelezionaTipo(userViewModel : UserViewModel,
                  onNavigateToSelezionaServizioBarba: () -> Unit,
                  onNavigateToSelezionaServizioCapelli: () -> Unit
) {

    val context = LocalContext.current

    //Column(modifier.fillMaxSize()) {


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        Box(Modifier.size(290.dp, 210.dp), contentAlignment = Alignment.Center){
            Image(painterResource(id = R.drawable.barba), contentDescription = "barba", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(290.dp, 210.dp)
                    .border(
                        BorderStroke(3.dp, my_gold), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onNavigateToSelezionaServizioBarba()
                        //userViewModel.logout()
                    }
                    .testTag("Home"),
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            )
            Text(text = "BARBA", color = my_white, fontSize = 40.sp,
                fontFamily = myFont,
                modifier = Modifier
                    .wrapContentSize()

                    .graphicsLayer {
                        rotationZ = -90f
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                    }
                    .align(AbsoluteAlignment.CenterLeft)
                    .padding(bottom = 26.dp)


            )

        }

        Box(Modifier.size(290.dp, 210.dp), contentAlignment = Alignment.Center){
            Image(painterResource(id = R.drawable.capelli), contentDescription = "capelli", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(290.dp, 210.dp)
                    .border(
                        BorderStroke(3.dp, my_gold), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onNavigateToSelezionaServizioCapelli()
                    },
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            )
            Text(text = "CAPELLI", color = my_white, fontSize = 40.sp,
                fontFamily = myFont,
                modifier = Modifier
                    .wrapContentSize()
                    .graphicsLayer {
                        rotationZ = -90f
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                    }
                    .align(AbsoluteAlignment.CenterLeft)
                    .padding(bottom = 60.dp)
            )

        }


    }
    //}









}



/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Prova()
}
 */




@Composable
fun ShowNavigationStack(navController: NavController) {
    // Ottieni la lista delle destinazioni nello stack di navigazione
    val backStackEntry = navController.currentBackStackEntryFlow.collectAsState(initial = null)

    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text("Navigation Stack:")
        backStackEntry.value?.let { entry ->
            Text(entry.destination.route ?: "Unknown Destination")
        }
    }
}