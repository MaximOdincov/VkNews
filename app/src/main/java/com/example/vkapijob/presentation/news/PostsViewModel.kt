package com.example.vkapijob.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapijob.data.repository.NewsRepository
import com.example.vkapijob.domain.Post
import com.example.vkapijob.extentions.mergeWith
import com.example.vkapijob.presentation.main.NavigationItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler{_, _ ->
        Log.d("exception", "exception")
    }
    private val postsFlow = repository.loadedPosts
    private val loadNextDataEvents = MutableSharedFlow<Unit>()
    private val loadNextDataFlow = flow{
        loadNextDataEvents.collect{
            emit(
                PostsScreenState.Posts(
                    posts = postsFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }
    val screenState = postsFlow
        .filter { it.isNotEmpty() }
        .map{ PostsScreenState.Posts(it) as PostsScreenState }
        .onStart { emit(PostsScreenState.Loading) }
        .mergeWith(loadNextDataFlow)


    fun loadPostsNext(){
        viewModelScope.launch{
            loadNextDataEvents.emit(Unit)
            repository.loadNextData()
        }
    }

    fun changeLikeStatus(post: Post){
        viewModelScope.launch(exceptionHandler) {
            if(post.isLiked) repository.deleteLike(post)
            else repository.addLike(post)
        }
    }

    private val _selectedNavItem = MutableStateFlow<NavigationItem>(NavigationItem.Home)
    val selectedNavItem = _selectedNavItem.asStateFlow()

    fun selectNavItem(navItem: NavigationItem){
        _selectedNavItem.value = navItem
    }

}