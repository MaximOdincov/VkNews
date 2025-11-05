package com.example.vkapijob.presentation.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vkapijob.presentation.news.PostsViewModel
import com.example.vkapijob.domain.Post
import com.example.vkapijob.presentation.news.PostsScreenState

@Composable
fun HomeScreen(paddingValues: PaddingValues, onCommentClickListener:(Post)->Unit, viewModelFactory: ViewModelProvider.Factory) {
    val viewModel: PostsViewModel = viewModel(factory = viewModelFactory)

    val screenState = viewModel.screenState.collectAsState(PostsScreenState.Initial)
    val currentState = screenState.value
    when(currentState) {
        is PostsScreenState.Posts -> CreatePosts(paddingValues, currentState.posts, viewModel, onCommentClickListener, currentState.nextDataIsLoading)
        is PostsScreenState.Initial -> {}
        is PostsScreenState.Loading -> {
            Loading()
        }
    }
}



@Composable
private fun CreatePosts(paddingValues: PaddingValues, posts: List<Post>, viewModel: PostsViewModel, onCommentClickListener:(Post)->Unit, isDataLoading: Boolean) {
    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(posts, key = { it.id }) { post ->
            Post(
                post = post,
                onLikeClickListener = {viewModel.changeLikeStatus(post)},
                onShareClickListener = {},
                onCommentClickListener = { onCommentClickListener(post) },
                onViewsClickListener = {}
            )
        }
        item{
            if(isDataLoading){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp), contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            else{
                SideEffect {
                    viewModel.loadPostsNext()
                }
            }
        }
    }
}


@Composable
fun Loading(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}