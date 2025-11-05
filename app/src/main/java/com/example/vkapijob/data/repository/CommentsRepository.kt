package com.example.vkapijob.data.repository

import com.example.vkapijob.data.mapper.CommentsMapper
import com.example.vkapijob.data.mapper.NewsMapper
import com.example.vkapijob.data.network.ApiFactory
import com.example.vkapijob.data.network.ApiService
import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.PostComment
import com.example.vkapijob.presentation.comments.CommentScreen
import com.vk.id.AccessToken
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.w3c.dom.Comment
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val token: AccessToken?,
    private val apiService: ApiService,
    private val mapper: CommentsMapper
){
    private val _comments = mutableListOf<PostComment>()
    private var currentOffset = 0
    private var isMoreComments = true

    private fun getAccessToken(): String {
        return token?.token ?: throw IllegalStateException("Token is null")
    }

    val loadEvents = MutableSharedFlow<Unit>()

    fun commentsFlow(post: Post): Flow<List<PostComment>> =
        loadEvents.map {
            if (isMoreComments) {
                val response = apiService.loadComments(
                    getAccessToken(),
                    post.communityId,
                    post.id,
                    SIZE_OF_COMMENTS_BUFFER,
                    currentOffset
                )

                val content = response.response
                val realOffset = content.realOffset ?: 0
                currentOffset += realOffset
                if (content.count < SIZE_OF_COMMENTS_BUFFER) isMoreComments = false
                _comments.addAll(mapper.mapResponseToComments(response))
            }
            _comments.toList()
        }

    suspend fun loadNewData(){
        loadEvents.emit(Unit)
    }
    
    companion object {
        private const val SIZE_OF_COMMENTS_BUFFER = 30
    }
}