package com.pipe.d.dev.mviarchwine.loginModule.intent

sealed class LoginIntent {
    data object CheckAuth: LoginIntent()
    data class SignIn(val username: String, val pin: String): LoginIntent()
}