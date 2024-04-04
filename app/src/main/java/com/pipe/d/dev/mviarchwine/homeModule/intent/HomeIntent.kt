package com.pipe.d.dev.mviarchwine.homeModule.intent

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.favoriteModule.intent.FavoriteIntent

sealed class HomeIntent {
    data object RequestWines: HomeIntent()
    data class AddWine(val wine: Wine) : HomeIntent()
}