package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthRepository
import com.business.renvest.data.repository.businessDisplayName

class PromotionsPresenter(
    private val view: PromotionsContract.View,
    private val authRepository: AuthRepository,
) : PromotionsContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authRepository.businessDisplayName(context))
        view.setupNav(R.id.nav_promos)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
