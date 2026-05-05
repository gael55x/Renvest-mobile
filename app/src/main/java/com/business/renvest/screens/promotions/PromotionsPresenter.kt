package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.R

class PromotionsPresenter(
    private val view: PromotionsContract.View,
    private val model: PromotionsModel,
) : PromotionsContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.businessDisplayName(context))
        view.setupNav(R.id.navPromos)
        view.displayPromotions(model.demoPromotions(context))
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }

    override fun onPromotionItemClicked(item: PromotionItem) {
        view.showComingSoon()
    }
}
