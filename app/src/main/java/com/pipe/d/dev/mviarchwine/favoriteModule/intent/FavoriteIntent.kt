package com.pipe.d.dev.mviarchwine.favoriteModule.intent

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine

sealed class FavoriteIntent {
    data object RequestWines: FavoriteIntent()
    data class AddWine(val wine: Wine) : FavoriteIntent()
    data class DeleteWine(val wine: Wine): FavoriteIntent()
}