package com.pipe.d.dev.mviarchwine.accountModule.intent

sealed class AccountIntent {
    data object RequestUser: AccountIntent()
    data object SignOut: AccountIntent()
}