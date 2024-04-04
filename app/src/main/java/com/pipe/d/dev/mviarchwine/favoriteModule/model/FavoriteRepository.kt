package com.pipe.d.dev.mviarchwine.favoriteModule.model

import com.pipe.d.dev.mviarchwine.R
import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.commonModule.utils.Constants

class FavoriteRepository(private val db: RoomDatabase) {
    fun getAllWines(): FavoriteState {
        val result = db.getAllWines()
        return FavoriteState.RequestWinesSuccess(result)
        /*return if (result.isNotEmpty()) {
            FavoriteState.RequestWinesSuccess(result)
        } else {
            FavoriteState.Fail(Constants.EC_REQUEST, R.string.room_request_fail)
        }*/

    }
    fun addWine(wine: Wine): FavoriteState {
        val result = db.addWine(wine)
        return if (result == -1L) {
            FavoriteState.Fail(Constants.EC_SAVE_WINE, R.string.room_save_fail)
        } else {
            FavoriteState.AddWineSuccess(R.string.room_save_success)
        }
    }
    fun deleteWine(wine: Wine): FavoriteState {
        val result = db.deleteWine(wine)
        return if (result == 0) {
            FavoriteState.Fail(Constants.EC_UPDATE_WINE, R.string.room_save_fail)
        } else {
            FavoriteState.DeleteWineSuccess(R.string.room_save_success)
        }
    }
}