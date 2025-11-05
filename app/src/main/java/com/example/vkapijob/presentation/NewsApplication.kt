package com.example.vkapijob.presentation

import android.app.Application
import com.example.vkapijob.di.ApplicationComponent
import com.example.vkapijob.di.DaggerApplicationComponent
import com.vk.id.VKID

class NewsApplication: Application() {
    val component: ApplicationComponent by lazy{
       DaggerApplicationComponent.factory().create(
           VKID.instance
       )
    }

    override fun onCreate() {
        super.onCreate()
        // initialize VKID early so component creation can safely use VKID.instance
        VKID.init(this)
    }
}