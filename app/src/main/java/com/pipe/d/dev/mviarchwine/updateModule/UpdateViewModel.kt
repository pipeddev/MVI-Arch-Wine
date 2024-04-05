package com.pipe.d.dev.mviarchwine.updateModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.favoriteModule.model.FavoriteState
import com.pipe.d.dev.mviarchwine.updateModule.intent.UpdateIntent
import com.pipe.d.dev.mviarchwine.updateModule.model.UpdateRepository
import com.pipe.d.dev.mviarchwine.updateModule.model.UpdateState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateViewModel(private val repository: UpdateRepository): ViewModel() {
    private val _state = MutableStateFlow<UpdateState>(UpdateState.Init)
    val state: StateFlow<UpdateState> = _state

    val channel = Channel<UpdateIntent>(Channel.UNLIMITED)

    init {
        setupIntent()
    }

    private fun setupIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow()
                .collect {intent ->
                    when (intent) {
                        is UpdateIntent.RequestWine -> requestWine(intent.id)
                        is UpdateIntent.UpdateWine -> updateWine(intent.wine)
                    }
                }
        }
    }

    private suspend fun requestWine(id: Double) {
        _state.value = UpdateState.ShowProgress

        try {
            withContext(Dispatchers.IO) {
                _state.value = repository.requestWine(id)
            }
        } finally {
            _state.value = UpdateState.HideProgress
        }
    }

    private suspend fun updateWine(wine: Wine) {
        _state.value = UpdateState.ShowProgress

        try {
            withContext(Dispatchers.IO) {
                _state.value = repository.updateWine(wine)
            }
        } finally {
            _state.value = UpdateState.HideProgress
        }
    }
}