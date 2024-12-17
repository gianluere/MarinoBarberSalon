package com.example.marinobarbersalon.Admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.my_drawer
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white

@Composable
fun NavDrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = R.drawable.ic_launcher_foreground.toString(),
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(10.dp),
            //colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
    }
}


@Composable
fun NavDrawerBody(
    items : List<NavDrawerItem>,
    currentRoute : String?,
    onClick: (NavDrawerItem) -> Unit,
) {
    items.forEachIndexed { index, navDrawerItem ->
        val isSelected = currentRoute == navDrawerItem.route
        NavigationDrawerItem(
            label = {
                Text(
                    text = navDrawerItem.title,
                    color =  my_white
                )
            }, selected = currentRoute == navDrawerItem.route, onClick = {
                onClick(navDrawerItem)
            }, icon = {
                Icon(
                    imageVector = if (currentRoute == navDrawerItem.route) {
                        navDrawerItem.selectedIcon
                    } else {
                        navDrawerItem.unselectedIcon
                    }, contentDescription = navDrawerItem.title
                )
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = my_drawer,         //Colore dello sfondo quando selezionato
            unselectedContainerColor = my_drawer,       // Colore dello sfondo non selezionato
            selectedIconColor = my_white,               // Colore icona selezionata
            unselectedIconColor = my_white,              // Colore icona non selezionata
            selectedTextColor = my_white,               // Colore testo selezionato
            unselectedTextColor = my_white               // Colore testo non selezionato
        )
        )
    }
}