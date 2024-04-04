package com.pipe.d.dev.mviarchwine.homeModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pipe.d.dev.mviarchwine.homeModule.model.HomeRepository

class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((HomeViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}