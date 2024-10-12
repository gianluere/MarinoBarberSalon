package com.example.marinobarbersalon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationAdmin(userViewModel : UserViewModel, adminViewModel: AdminViewModel) {
    val adminNavController = rememberNavController()
    NavHost(adminNavController, startDestination = ScreenAdmin.Prova.route){
        composable(ScreenAdmin.Prova.route){
            Prova()

        }
    }

}
sealed class ScreenAdmin(val route:String ){
    object Prova : ScreenAdmin("prova")


}