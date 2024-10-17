import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.marinobarbersalon.Admin.AdminNavItem
import com.example.marinobarbersalon.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNavDrawer(modifier: Modifier = Modifier) {
     val navItem = listOf(
         AdminNavItem(Screen.HomeAdmin.route, Icons.Filled.Home ,Icons.Outlined.Home)
     )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier= Modifier.height(16.dp))

                navItem.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label= {
                            Text(text= item.description)
                        } ,
                        selected= index==selectedItemIndex  ,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if(index == selectedItemIndex){
                                    item.selectedIcon
                                } else item.unselectedItem,
                                contentDescription = item.description
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }



        }
        ,
        drawerState = drawerState,




    ) {
        Scaffold (
            topBar = {
                TopAppBar( title = {
                    Text(text = "Prova se va")
                },
                    navigationIcon = {
                        IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }


                        }) {
                            Icon(
                                imageVector= Icons.Default.Menu,
                                contentDescription= "Menu"
                            )

                        }
                    }
                )
            }
        ){
        /*TODO*/

    }
}}