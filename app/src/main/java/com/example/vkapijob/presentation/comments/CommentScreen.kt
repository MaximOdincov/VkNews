package com.example.vkapijob.presentation.comments

import androidx.collection.objectIntMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.PostComment
import com.example.vkapijob.presentation.NewsApplication
import com.example.vkapijob.presentation.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    onBackPressed: () -> Unit,
    post: Post
) {
    val component = (LocalContext.current.applicationContext as NewsApplication)
        .component.getCommentsScreenComponentFactory()
        .create(post)

    val viewModel: CommentsViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState()
    val currentState = screenState.value
    when (currentState) {
        is CommentsScreenState.Comments -> {CommentScreen(
            comments = currentState.comments,
            onBackPressed = onBackPressed,
            isDataLoading = currentState.isDataLoading,
            onEndOfComments = {viewModel.loadNextData()})
        }
        is CommentsScreenState.Initial -> {}
        is CommentsScreenState.Loading -> {Loading()}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(comments: List<PostComment>, isDataLoading: Boolean, onBackPressed:()->Unit, onEndOfComments: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Comments")
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                })
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(items = comments, key = { it.id }) { comment ->
                Comment(comment)
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
                        onEndOfComments()
                    }
                }
            }
        }

    }
}

@Composable
fun Comment(postComment: PostComment){
    Column(Modifier.padding(6.dp)){
        Row {
            AsyncImage(
                model = postComment.authorAvatarUrl,
                contentDescription = "authorOfCommentAvatar",
                modifier = Modifier.size(56.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(20.dp))
            Column{
                Row{
                    Text(text = postComment.author, fontSize = 20.sp, fontStyle = FontStyle.Italic,
                        modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onPrimary)
                    Text(postComment.publicationDate, modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onSecondary)
                }
                Text(postComment.commentText, fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
        Spacer(Modifier.height(15.dp))
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