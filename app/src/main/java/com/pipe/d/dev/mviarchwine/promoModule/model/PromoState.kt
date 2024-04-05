package com.pipe.d.dev.mviarchwine.promoModule.model

import com.pipe.d.dev.mviarchwine.commonModule.entities.Promo

sealed class PromoState {
    data object Init: PromoState()
    data class Fail(val code: Int, val msgRes: Int): PromoState()
    data class RequestPromoSuccess(val list: List<Promo>): PromoState()
}