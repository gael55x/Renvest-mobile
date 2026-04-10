package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore

class PromotionsPresenter(
    private val view: PromotionsContract.View,
    private val authStore: AuthStore,
) : PromotionsContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authStore.businessDisplayName(context))
        view.setupNav(R.id.navPromos)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
