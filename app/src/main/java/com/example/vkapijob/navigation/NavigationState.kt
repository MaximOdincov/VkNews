package com.example.vkapijob.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.StatisticItem
import com.example.vkapijob.presentation.main.NavigationItem

class NavigationState(val navHostController: NavHostController){
    fun navigateTo(route: String){
        navHostController.navigate(route){
            launchSingleTop = true
            popUpTo(navHostController.graph.findStartDestination().id){
                saveState = true
            }
            restoreState = true
        }
    }

    fun navigateToComments(post: Post){
        navHostController.navigate(Screen.Comments.getRouteWithArgs(post))
    }
}

@Composable
fun rememberNavigationState(navHostController: NavHostController = rememberNavController()): NavigationState{
    return remember {
        NavigationState(navHostController)
    }
}