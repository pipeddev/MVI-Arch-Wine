package com.pipe.d.dev.mviarchwine.accountModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pipe.d.dev.mviarchwine.accountModule.intent.AccountIntent
import com.pipe.d.dev.mviarchwine.accountModule.model.AccountRepository
import com.pipe.d.dev.mviarchwine.accountModule.model.AccountState
import com.pipe.d.dev.mviarchwine.mainModule.MainActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository): ViewModel() {
    private val _state = MutableStateFlow<AccountState>(AccountState.Init)
    val state: StateFlow<AccountState> = _state

    val channel = Channel<AccountIntent>(Channel.UNLIMITED)

    init {
        setupIntent()
    }

    private fun setupIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow()
                .collect {intent ->
                    when (intent) {
                        is AccountIntent.RequestUser -> getCurrentUser()
                        is AccountIntent.SignOut -> signOut()
                    }

                }
        }
    }

    private suspend fun getCurrentUser() {
        _state.value = AccountState.ShowProgress

        try {
            _state.value = repository.getCurrentUser()
        } finally {
            _state.value = AccountState.HideProgress
        }
    }

    private suspend fun signOut() {
        _state.value = AccountState.ShowProgress

        try {
            _state.value = repository.signOut()
        } finally {
            _state.value = AccountState.HideProgress
        }
    }
}