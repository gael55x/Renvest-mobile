package com.business.renvest.screens.loyalty

import com.business.renvest.R

class LoyaltyPresenter(
    private val view: LoyaltyContract.View,
) : LoyaltyContract.Presenter {

    override fun onViewReady() {
        view.setStubTitle(R.string.feature_loyalty_title)
    }
}
