package com.example.marinobarbersalon.Admin

import androidx.compose.ui.graphics.vector.ImageVector

data class NavDrawerItem(
    val title : String,
    val selectedIcon: ImageVector,
    val unselectedIcon : ImageVector,
    val route : String
)
