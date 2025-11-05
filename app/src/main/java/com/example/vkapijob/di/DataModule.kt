package com.example.vkapijob.di

import com.example.vkapijob.data.network.ApiFactory
import com.example.vkapijob.data.network.ApiService
import com.vk.id.AccessToken
import com.vk.id.VKID
import dagger.Module
import dagger.Provides

@Module
interface DataModule {


    companion object{
        @ApplicationScope
        @Provides
        fun providesApiService(): ApiService{
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun providesAccessToken (): AccessToken?{
            return VKID.instance.accessToken
        }
    }
}