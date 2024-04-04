package com.pipe.d.dev.mviarchwine.homeModule.model

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.favoriteModule.model.FavoriteState

sealed class HomeState {
    data object Init: HomeState()
    data object  ShowProgress: HomeState()
    data object HideProgress: HomeState()
    data class RequestWinesSuccess(val list: List<Wine>): HomeState()
    data class AddWineSuccess(val msgRes: Int) : HomeState()
    data class Fail(val code: Int, val msgRes: Int): HomeState()
}