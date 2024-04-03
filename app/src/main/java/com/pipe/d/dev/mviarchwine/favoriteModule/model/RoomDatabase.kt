package com.pipe.d.dev.mviarchwine.favoriteModule.model

import com.pipe.d.dev.mviarchwine.WineApplication
import com.pipe.d.dev.mviarchwine.commonModule.dataAccess.room.WineDao
import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine

class RoomDatabase {
    private val dao: WineDao by lazy { WineApplication.database.wineDao() }

    fun getAllWines() = dao.getAllWines()
    fun addWine(wine: Wine) = dao.addWine(wine)
    fun deleteWine(wine: Wine) = dao.deleteWine(wine)
}