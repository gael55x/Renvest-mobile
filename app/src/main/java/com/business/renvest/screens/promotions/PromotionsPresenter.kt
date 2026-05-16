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
        val items = model.loadPromotions()
        val counts = model.localDataCounts()
        view.bindPromotionsHero(
            activePromotions = counts.promotionsActive.toString(),
            customerRecords = counts.customers.toString(),
            activityRecords = counts.activityEvents.toString(),
        )
        view.displayPromotions(items)
        view.setPromotionsEmptyVisible(items.isEmpty())
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }

    @Suppress("UnusedParameter")
    override fun onPromotionItemClicked(item: PromotionItem) {
        view.showComingSoon()
    }
}
