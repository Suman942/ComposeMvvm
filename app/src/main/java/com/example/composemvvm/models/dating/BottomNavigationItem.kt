package com.example.composemvvm.models.dating

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title:String,
    val selectedItem: ImageVector,
    val unselectedItem : ImageVector,
    val badgeCount : Int? = null,
    val hasNews:Boolean
)
