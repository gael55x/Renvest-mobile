package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.R

class PromotionsPresenter(
    private val view: PromotionsContract.View,
    private val model: PromotionsModel,
) : PromotionsContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.headerBusinessName(context))
        view.setupNav(R.id.nav_promos)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
