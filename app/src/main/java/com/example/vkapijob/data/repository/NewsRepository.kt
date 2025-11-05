package com.example.vkapijob.data.repository

import androidx.lifecycle.viewModelScope
import com.example.vkapijob.data.mapper.NewsMapper
import com.example.vkapijob.data.model.LikesCountDto
import com.example.vkapijob.data.model.LikesCountResponseDto
import com.example.vkapijob.data.network.ApiFactory
import com.example.vkapijob.data.network.ApiService
import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.StatisticItem
import com.example.vkapijob.domain.StatisticType
import com.example.vkapijob.extentions.mergeWith
import com.example.vkapijob.presentation.news.PostsScreenState
import com.vk.id.AccessToken
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val token: AccessToken?,
    private val apiService: ApiService,
    private val mapper: NewsMapper
){
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<Post>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var nextFrom: String? = null
    private val _posts: MutableList<Post> = mutableListOf()
    val posts: List<Post>
        get() =  _posts.toList()

    val loadedPosts: StateFlow<List<Post>> = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom

            if (startFrom == null && posts.isNotEmpty()) {
                emit(posts)
                return@collect
            }

            val response = if (startFrom == null) {
                ApiFactory.apiService.loadPosts(getAccessToken())
            } else {
                ApiFactory.apiService.loadPosts(token = getAccessToken(), startFrom = startFrom)
            }
            nextFrom = response.newsContent.nextFrom
            val mappedPosts = mapper.mapResponseToPosts(response)
            _posts.addAll(mappedPosts)
            emit(posts)
            return@collect
        }
    }.retry{
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = posts)

    suspend fun loadNextData(){
        nextDataNeededEvents.emit(Unit)
    }

    suspend fun addLike(post: Post){
        val response = apiService.addLike(getAccessToken(), post.communityId, post.id)
        changeLikeCount(response, post, false)
        refreshedListFlow.emit(posts)
    }

    suspend fun deleteLike(post: Post){
        val response = apiService.deleteLike(getAccessToken(), post.communityId, post.id)
        changeLikeCount(response, post, true)
        refreshedListFlow.emit(posts)
    }

    private fun changeLikeCount(response: LikesCountResponseDto, post: Post, isLiked: Boolean){
        val newLikesCount = response.likes.count
        val newStatistics = post.statistics.toMutableList().apply{
            removeIf{it.type == StatisticType.LIKES}
            add(StatisticItem(StatisticType.LIKES, newLikesCount))
        }
        val newPost = post.copy(statistics = newStatistics, isLiked = !isLiked)
        val postIndex = _posts.indexOf(post)
        _posts[postIndex] = newPost
    }

    private fun getAccessToken(): String {
        return token?.token ?: throw IllegalStateException("token is null")
    }


    companion object{
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}