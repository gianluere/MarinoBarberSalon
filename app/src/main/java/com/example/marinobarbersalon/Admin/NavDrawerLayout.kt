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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.marinobarbersalon.R

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
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = R.drawable.ic_launcher_foreground.toString(),
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(10.dp)
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
        NavigationDrawerItem(
            label = {
                Text(text = navDrawerItem.title)
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}