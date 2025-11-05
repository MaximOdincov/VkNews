package com.example.vkapijob.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapijob.presentation.main.LoginState
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val vkid: VKID
): ViewModel() {
    private val _authState = MutableStateFlow<LoginState>(LoginState.Initial)
    val authState = _authState.asStateFlow()

    private val getUserCallback = object : VKIDGetUserCallback{
        override fun onSuccess(user: VKIDUser) {
            onAuthSuccess()
        }
        override fun onFail(fail: VKIDGetUserFail) {
            onAuthFail()
        }
    }

    init {
        viewModelScope.launch {
            vkid.getUserData(getUserCallback)
            }
        }

    fun onAuthSuccess(){
        _authState.value = LoginState.Authorized
    }

    fun onAuthFail(){
        _authState.value = LoginState.NotAuthorized
    }
}