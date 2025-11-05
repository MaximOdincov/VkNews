package com.example.vkapijob.presentation.main

sealed class LoginState {
    object Authorized: LoginState()
    object NotAuthorized: LoginState()
    object Initial: LoginState()
}