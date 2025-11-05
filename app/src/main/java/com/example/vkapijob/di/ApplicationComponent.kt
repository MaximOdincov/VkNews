package com.example.vkapijob.di

import com.example.vkapijob.presentation.main.MainActivity
import com.vk.id.VKID
import dagger.BindsInstance
import dagger.Component;

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)

    fun getCommentsScreenComponentFactory(): CommentScreenComponent.Factory

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance vkid: VKID
        ): ApplicationComponent
    }
}