package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthRepository
import com.business.renvest.data.repository.businessDisplayName

class AiEngagementAdvisorPresenter(
    private val view: AiEngagementAdvisorContract.View,
    private val authRepository: AuthRepository,
) : AiEngagementAdvisorContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authRepository.businessDisplayName(context))
        view.setupNav(R.id.nav_home)
        view.setEngagementProgress(74)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
