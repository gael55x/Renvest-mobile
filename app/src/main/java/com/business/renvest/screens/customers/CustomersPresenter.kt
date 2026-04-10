package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore

class CustomersPresenter(
    private val view: CustomersContract.View,
    private val authStore: AuthStore,
) : CustomersContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authStore.businessDisplayName(context))
        view.setupNav(R.id.navCustomers)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
