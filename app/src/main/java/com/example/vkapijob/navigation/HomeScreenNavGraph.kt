package com.example.vkapijob.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.vkapijob.domain.Post
import com.google.gson.Gson

fun NavGraphBuilder.homeScreenNavGraph(
    newsScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (post: Post) -> Unit
){
    navigation(
        startDestination = Screen.News.route,
        route = Screen.Home.route,
    ){
        composable(Screen.News.route){
            newsScreenContent()
        }
        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                navArgument(Screen.KEY_POST){
                    type = NavType.StringType
                },
            )
        ){
            val postJson = it.arguments?.getString(Screen.KEY_POST) ?: ""
            val post = Gson().fromJson<Post>(postJson, Post::class.java)
            commentsScreenContent(post)
        }
    }
}