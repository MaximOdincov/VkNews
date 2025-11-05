package com.example.vkapijob.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vkapijob.R
import com.example.vkapijob.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val labelResId: Int,
    val icon: ImageVector
) {

    object Home: NavigationItem(Screen.Home, R.string.nav_item_home, Icons.Outlined.Home)

    object Favourite: NavigationItem(
        Screen.Favourite,
        R.string.nav_item_favourite, Icons.Outlined.Favorite
    )

    object Profile: NavigationItem(Screen.Profile, R.string.nav_item_profile, Icons.Outlined.Person)
}