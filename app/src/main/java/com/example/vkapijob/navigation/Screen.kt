package com.example.vkapijob.navigation


import android.net.Uri
import com.example.vkapijob.domain.Post
import com.google.gson.Gson
import org.w3c.dom.Comment

sealed class Screen(
    val route : String
){
    object News: Screen(ROUTE_NEWS)
    object Favourite: Screen(ROUTE_FAVOURITE)
    object Profile: Screen(ROUTE_PROFILE)
    object Comments: Screen(ROUTE_COMMENTS){
        private const val ROUTE_FOR_ARGS = "comments"
        fun getRouteWithArgs(post: Post): String{
            val postJson = Gson().toJson(post)
            return "$ROUTE_FOR_ARGS/${postJson.encode()}"
        }
    }
    object Home: Screen(ROUTE_HOME)


    companion object{
        const val KEY_POST = "post"
        const val ROUTE_HOME = "home"
        const val ROUTE_NEWS= "news"
        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_PROFILE = "profile"
        const val ROUTE_COMMENTS = "comments/{$KEY_POST}"
    }
}

fun String.encode(): String{
    return Uri.encode(this)
}