package com.example.vkapijob.presentation.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapijob.data.repository.CommentsRepository
import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.PostComment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val post: Post,
    private val repository: CommentsRepository
):ViewModel() {
    private val _loadEvents = MutableSharedFlow<Unit>()
    val loadEvents = _loadEvents.asSharedFlow()

    val screenState: StateFlow<CommentsScreenState> = repository
        .commentsFlow(post)
        .map<List<PostComment>, CommentsScreenState> { comments ->
            CommentsScreenState.Comments(comments)
        }
        .onStart {
            emit(CommentsScreenState.Loading)
            loadNextData()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = CommentsScreenState.Initial
        )

    fun loadNextData() {
        viewModelScope.launch {
            repository.loadNewData()
        }
    }
}