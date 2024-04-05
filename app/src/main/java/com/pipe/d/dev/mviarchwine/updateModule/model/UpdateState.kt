package com.pipe.d.dev.mviarchwine.updateModule.model

import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine

sealed class UpdateState {
    data object Init: UpdateState()
    data object  ShowProgress: UpdateState()
    data object HideProgress: UpdateState()
    data class RequestWineSuccess(val wine: Wine): UpdateState()
    data object UpdateWineSuccess : UpdateState()
    data class Fail(val code: Int, val msgRes: Int): UpdateState()
}