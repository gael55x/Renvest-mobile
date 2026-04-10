package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore

class AiEngagementAdvisorPresenter(
    private val view: AiEngagementAdvisorContract.View,
    private val authStore: AuthStore,
) : AiEngagementAdvisorContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authStore.businessDisplayName(context))
        view.setupNav(R.id.nav_home)
        view.setEngagementProgress(74)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
