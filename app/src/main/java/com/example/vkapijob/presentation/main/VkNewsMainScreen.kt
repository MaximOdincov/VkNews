package com.example.vkapijob.presentation.main
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vkapijob.navigation.AppNavGraph
import com.example.vkapijob.navigation.rememberNavigationState
import com.example.vkapijob.presentation.news.HomeScreen
import com.example.vkapijob.presentation.comments.CommentScreen
import androidx.lifecycle.ViewModelProvider
@Composable
fun MainScreen(viewModelFactory: ViewModelProvider.Factory){
    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favourite,
                    NavigationItem.Profile
                )
                items.forEach {item ->

                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if(!selected){
                                navigationState.navigateTo(item.screen.route)
                            }
                                  },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(text = stringResource(id = item.labelResId)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
        }
    ){paddingValues ->
                AppNavGraph(
            navController = navigationState.navHostController,
            newsScreenContent =
                {
                    HomeScreen(
                        paddingValues = paddingValues,
                        onCommentClickListener = { post ->
                            navigationState.navigateToComments(post)
                        },
                        viewModelFactory = viewModelFactory
                    )
                },
            commentsScreenContent = {post ->
                CommentScreen({ navigationState.navHostController.popBackStack() }, post)
            },
            favouriteScreenContent = {Text("fdfdf")},
            profileScreenContent = {Text("ffdfdfsfsdf")}
        )
    }
}