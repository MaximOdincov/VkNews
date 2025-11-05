package com.example.vkapijob.di

import androidx.lifecycle.ViewModel
import com.example.vkapijob.presentation.main.MainViewModel
import com.example.vkapijob.presentation.news.PostsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    @Binds
    fun bindPostsViewModel(viewModel: PostsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}