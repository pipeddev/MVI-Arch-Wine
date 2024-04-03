package com.pipe.d.dev.mviarchwine.favoriteModule.model

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine

sealed class FavoriteState {
    data object Init: FavoriteState()
    data object  ShowProgress: FavoriteState()
    data object HideProgress: FavoriteState()

    data class RequestWinesSuccess(val list: List<Wine>): FavoriteState()
    data class AddWineSuccess(val msgRes: Int) : FavoriteState()
    data class DeleteWineSuccess(val msgRes: Int): FavoriteState()
    data class Fail(val code: Int, val msgRes: Int): FavoriteState()
}