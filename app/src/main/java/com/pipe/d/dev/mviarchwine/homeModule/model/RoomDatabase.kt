package com.pipe.d.dev.mviarchwine.homeModule.model

import com.pipe.d.dev.mviarchwine.WineApplication
import com.pipe.d.dev.mviarchwine.commonModule.dataAccess.room.WineDao
import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine

class RoomDatabase {
    private val dao: WineDao by lazy { WineApplication.database.wineDao() }

    fun addWine(wine: Wine) = dao.addWine(wine)
}