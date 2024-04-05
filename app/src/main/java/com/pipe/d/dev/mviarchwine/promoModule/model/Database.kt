package com.pipe.d.dev.mviarchwine.promoModule.model

import com.pipe.d.dev.mviarchwine.commonModule.dataAccess.local.getAllPromos

class Database {
    fun getPromos() = getAllPromos()
}