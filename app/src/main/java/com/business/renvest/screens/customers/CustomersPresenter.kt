package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.R

class CustomersPresenter(
    private val view: CustomersContract.View,
    private val model: CustomersModel,
) : CustomersContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.headerBusinessName(context))
        view.setupNav(R.id.nav_customers)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
