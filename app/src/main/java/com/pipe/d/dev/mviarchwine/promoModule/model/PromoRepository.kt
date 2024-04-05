package com.pipe.d.dev.mviarchwine.promoModule.model

import com.pipe.d.dev.mviarchwine.R
import com.pipe.d.dev.mviarchwine.commonModule.utils.Constants

class PromoRepository(private val db: Database) {
    fun getPromos(): PromoState {
        val result = db.getPromos()
        return if(result.isNotEmpty()) {
            PromoState.RequestPromoSuccess(result)
        } else {
            PromoState.Fail(Constants.EC_REQUEST, R.string.promo_request_fail)
        }
    }
}