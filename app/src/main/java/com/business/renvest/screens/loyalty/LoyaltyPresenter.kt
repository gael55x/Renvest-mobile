package com.business.renvest.screens.loyalty

class LoyaltyPresenter(
    private val view: LoyaltyContract.View,
    private val model: LoyaltyModel,
) : LoyaltyContract.Presenter {

    override fun onViewReady() {
        view.setStubTitle(model.stubTitleRes())
    }
}
