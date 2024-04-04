package com.pipe.d.dev.mviarchwine.loginModule.model

import com.pipe.d.dev.mviarchwine.R
import com.pipe.d.dev.mviarchwine.commonModule.dataAccess.local.FakeFirebaseAuth
import com.pipe.d.dev.mviarchwine.commonModule.utils.Constants

class LoginRepository(private val auth:FakeFirebaseAuth) {

    suspend fun checkAuth(): LoginState {
        val result = auth.isValidAuth()
        return if (result)
            LoginState.AuthValid
        else {
            LoginState.Fail(Constants.EC_AUTH, R.string.login_auth_fail)
        }
    }

    suspend fun login(username: String, pin: String): LoginState {
        val result = auth.login(username, pin)
        return if (result)
            LoginState.AuthValid
        else {
            LoginState.Fail(Constants.EC_AUTH, R.string.login_login_fail)
        }
    }
}