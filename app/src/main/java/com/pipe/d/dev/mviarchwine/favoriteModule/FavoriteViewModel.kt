package com.pipe.d.dev.mviarchwine.favoriteModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pipe.d.dev.mviarchwine.accountModule.intent.AccountIntent
import com.pipe.d.dev.mviarchwine.accountModule.model.AccountState
import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.favoriteModule.intent.FavoriteIntent
import com.pipe.d.dev.mviarchwine.favoriteModule.model.FavoriteRepository
import com.pipe.d.dev.mviarchwine.favoriteModule.model.FavoriteState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteRepository): ViewModel() {
    private val _state = MutableStateFlow<FavoriteState>(FavoriteState.Init)
    val state: StateFlow<FavoriteState> = _state

    val channel = Channel<FavoriteIntent>(Channel.UNLIMITED)

    init {
        setupIntent()
    }

    private fun setupIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow()
                .collect {intent ->
                    when (intent) {
                        is FavoriteIntent.RequestWines -> getAllWines()
                        is FavoriteIntent.AddWine -> addWine(intent.wine)
                        is FavoriteIntent.DeleteWine -> deleteWine(intent.wine)
                    }
                }
        }
    }

    private fun getAllWines() {
        _state.value = FavoriteState.ShowProgress

        try {
            _state.value = repository.getAllWines()
        } finally {
            _state.value = FavoriteState.HideProgress
        }
    }

    private fun addWine(wine: Wine) {
        _state.value = FavoriteState.ShowProgress

        try { // TODO: WithContext...
            _state.value = repository.addWine(wine)
        } finally {
            _state.value = FavoriteState.HideProgress
        }
    }
    private fun deleteWine(wine: Wine) {
        _state.value = FavoriteState.ShowProgress

        try {
            _state.value = repository.deleteWine(wine)
        } finally {
            _state.value = FavoriteState.HideProgress
        }
    }




}