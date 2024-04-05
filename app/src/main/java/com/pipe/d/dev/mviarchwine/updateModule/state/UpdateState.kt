package com.pipe.d.dev.mviarchwine.updateModule.state

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.favoriteModule.model.FavoriteState
import com.pipe.d.dev.mviarchwine.updateModule.intent.UpdateIntent

sealed class UpdateState {
    data object Init: UpdateState()
    data object  ShowProgress: UpdateState()
    data object HideProgress: UpdateState()

    data class RequestWineSuccess(val wine: Wine): UpdateState()
    data object UpdateWineSuccess : UpdateState()

    data class Fail(val code: Int, val msgRes: Int): UpdateState()
}