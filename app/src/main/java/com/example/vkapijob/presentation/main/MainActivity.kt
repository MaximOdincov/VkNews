package com.example.vkapijob.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapijob.presentation.NewsApplication
import com.example.vkapijob.presentation.ViewModelFactory
import com.example.vkapijob.presentation.main.LoginState
import com.example.vkapijob.ui.theme.VkApiJobTheme
import com.vk.id.VKID
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.compose.onetap.OneTap
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as NewsApplication).component
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    component.inject(this)
    enableEdgeToEdge()
        setContent {
            VkApiJobTheme {
                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                val authState = viewModel.authState.collectAsState()

                when(authState.value){
                    is LoginState.Authorized -> {MainScreen(viewModelFactory = viewModelFactory)}
                    is LoginState.NotAuthorized -> {AuthScreen { ScreenWithVKIDButton(viewModel) }}
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ScreenWithVKIDButton(viewModel: MainViewModel) {
    val scopes = setOf(
        "vkid.personal_info",
        "wall",
        "friends",
        "groups",
        "photos",
        "offline")

    OneTap(
        onAuth = { oAuth, token ->
            Log.d("keyT", token.toString())
            viewModel.onAuthSuccess()
        },
        onFail = { oAuth, e ->
            viewModel.onAuthFail()
        },
        signInAnotherAccountButtonEnabled = true,
        authParams = VKIDAuthUiParams.Builder().apply {this.scopes = scopes}.build())
}
