package com.pipe.d.dev.mviarchwine.promoModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pipe.d.dev.mviarchwine.loginModule.intent.LoginIntent
import com.pipe.d.dev.mviarchwine.loginModule.model.LoginState
import com.pipe.d.dev.mviarchwine.promoModule.intent.PromoIntent
import com.pipe.d.dev.mviarchwine.promoModule.model.PromoRepository
import com.pipe.d.dev.mviarchwine.promoModule.model.PromoState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class PromoViewModel(private val repository: PromoRepository): ViewModel() {
    private val _state = MutableStateFlow<PromoState>(PromoState.Init)
    val state: StateFlow<PromoState> = _state

    val channel = Channel<PromoIntent>(Channel.UNLIMITED)

    init {
        setupIntent()
    }

    private fun setupIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow()
                .collect {intent ->
                    when (intent) {
                        is PromoIntent.RequestPromos -> requestPromos()
                    }
                }
        }
    }

    private fun requestPromos() {
        _state.value = repository.getPromos()
    }
}