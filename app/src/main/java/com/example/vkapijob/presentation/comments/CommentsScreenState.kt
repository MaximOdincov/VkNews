package com.example.vkapijob.presentation.comments

import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.PostComment

sealed class CommentsScreenState{
    object Loading: CommentsScreenState()
    object Initial: CommentsScreenState()
    data class Comments(val comments: List<PostComment>, val isDataLoading: Boolean = false): CommentsScreenState()
}