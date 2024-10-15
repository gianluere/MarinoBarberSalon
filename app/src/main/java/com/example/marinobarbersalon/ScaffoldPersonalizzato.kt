package com.example.marinobarbersalon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white



@Composable
fun ScaffoldPersonalizzato(titolo : String,
                           showIcon : Boolean,
                           onBack : () -> Unit = {},
                           content : @Composable () -> Unit = {}) {
    Scaffold(
        containerColor = Color(0xFF333333),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 25.dp),
                    contentAlignment = Alignment.Center
                ){

                    if (showIcon){
                        IconButton(onClick = {onBack()},
                            modifier = Modifier.size(25.dp).align(Alignment.CenterStart)) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
                                tint = my_white,
                                modifier = Modifier.size(25.dp))
                        }
                    }

                    Text(
                        text = titolo,
                        color = my_white,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )


                }


                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),// altezza della bottom bar
                    thickness = 2.dp,
                    color = my_gold
                )
            }
        },
        bottomBar = { BarraNavigazione() }
    ) {innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()) {
            content()
        }

    }

}


@Composable
fun BarraNavigazione() {
    val navItemList = listOf(
        NavItem("", ImageVector.vectorResource(R.drawable.sharp_content_cut_24)),
        NavItem("", Icons.Outlined.AccountCircle),
        NavItem("", Icons.Outlined.ShoppingCart),
        NavItem("", Icons.Default.Settings)
    )
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

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