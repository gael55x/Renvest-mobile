package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.R

class CustomersPresenter(
    private val view: CustomersContract.View,
    private val model: CustomersModel,
) : CustomersContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.businessDisplayName(context))
        view.setupNav(R.id.navCustomers)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
