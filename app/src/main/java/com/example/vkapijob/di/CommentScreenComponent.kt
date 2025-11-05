package com.example.vkapijob.di

import com.example.vkapijob.domain.Post
import com.example.vkapijob.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        CommentsViewModelModule::class
    ]
)
interface CommentScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

@Subcomponent.Factory
    interface Factory{

        fun create(
            @BindsInstance post: Post
        ): CommentScreenComponent
    }
}