package com.example.marinobarbersalon.Admin


import androidx.compose.ui.graphics.vector.ImageVector
import com.example.marinobarbersalon.Screen

data class AdminNavItem (
    val description : String,
    val selectedIcon: ImageVector,
    val unselectedItem : ImageVector,
    val route : Screen? = null
)

