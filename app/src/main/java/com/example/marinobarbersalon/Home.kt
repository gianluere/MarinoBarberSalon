package com.example.marinobarbersalon

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.ui.theme.myFont

import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white



@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSelezionaServizioBarba: () -> Unit,
    onNavigateToSelezionaServizioCapelli: () -> Unit,
    userViewModel: UserViewModel
) {

    val activity = LocalContext.current as? ComponentActivity


    BackHandler {
        activity?.finish()
    }


    val userState by userViewModel.userState.collectAsState()
    
    LaunchedEffect(userState.state){
        when(userState.state){
            is AuthState.Unauthenticated -> onNavigateToLogin()
            else -> Unit
        }
    }


    val navItemList = listOf(
        NavItem("", ImageVector.vectorResource(R.drawable.sharp_content_cut_24)),
        NavItem("", Icons.Outlined.AccountCircle),
        NavItem("", Icons.Outlined.ShoppingCart),
        NavItem("", Icons.Default.Settings)
    )

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        containerColor = Color(0xFF333333),
        modifier = Modifier.fillMaxSize(),
        topBar = {
                 Column(Modifier.fillMaxWidth(),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                     Text(
                         text = "BENVENUTO " + userState.nome.toString().uppercase(),
                         color = my_white,
                         fontSize = 25.sp,
                         fontWeight = FontWeight.Bold,
                         modifier = Modifier.padding(bottom = 25.dp)
                     )

                     HorizontalDivider(
                         modifier = Modifier
                             .fillMaxWidth(),// altezza della bottom bar
                         thickness = 2.dp,
                         color = my_gold
                     )
                 }
        },
        bottomBar = {
            Box{

                NavigationBar(containerColor = Color(0xFF333333)) {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = false,
                            onClick = { selectedIndex = index },
                            icon = {
                                Icon(imageVector = navItem.icon, contentDescription = "Icon",
                                    tint = if (index != selectedIndex){
                                        my_white
                                    }else{
                                        my_bordeaux
                                    },
                                    modifier = Modifier.size(45.dp))
                            }
                        )
                    }

                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = my_white
                )
            }

        }
    ){innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()) {
            SelezionaTipo(userViewModel,
                onNavigateToSelezionaServizioBarba,
                onNavigateToSelezionaServizioCapelli
            )

        }

    }
    
    /*Text(text = "HOME")
    
    TextButton(onClick = {userViewModel.logout()}) {
        Text(text = "ESCI")
    }*/
    
    //ShowNavigationStack(navController = navController)



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
                    },
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

        Button(onClick = {
            userViewModel.logout()
        }) {
            Text(text = "LOGOUT")
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