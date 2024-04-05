package com.pipe.d.dev.mviarchwine.promoModule.intent

sealed class PromoIntent {
    data object RequestPromos: PromoIntent()
}