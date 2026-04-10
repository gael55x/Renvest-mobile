package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthRepository
import com.business.renvest.data.repository.businessDisplayName

class CustomersPresenter(
    private val view: CustomersContract.View,
    private val authRepository: AuthRepository,
) : CustomersContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authRepository.businessDisplayName(context))
        view.setupNav(R.id.nav_customers)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
