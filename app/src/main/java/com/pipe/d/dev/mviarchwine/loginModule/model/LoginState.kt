package com.pipe.d.dev.mviarchwine.loginModule.model

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.homeModule.model.HomeState

sealed class LoginState {
    data object Init: LoginState()
    data object  ShowProgress: LoginState()
    data object HideProgress: LoginState()
    data class Fail(val code: Int, val msgRes: Int): LoginState()
    data object AuthValid: LoginState()
    data object  LoginSuccess: LoginState()
}