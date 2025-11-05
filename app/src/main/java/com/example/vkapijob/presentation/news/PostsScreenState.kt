package com.example.vkapijob.presentation.news

import com.example.vkapijob.domain.Post

sealed class PostsScreenState{
    data class Posts(val posts: List<Post>, val nextDataIsLoading: Boolean = false): PostsScreenState()

    object Initial: PostsScreenState()

    object Loading: PostsScreenState()
}