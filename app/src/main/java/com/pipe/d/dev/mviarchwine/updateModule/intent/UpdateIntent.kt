package com.pipe.d.dev.mviarchwine.updateModule.intent

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine

sealed class UpdateIntent {
    data class RequestWine(var id: Double): UpdateIntent()
    data class UpdateWine(var wine: Wine): UpdateIntent()
}