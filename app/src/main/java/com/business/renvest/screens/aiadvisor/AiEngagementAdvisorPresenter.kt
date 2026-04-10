package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.R

class AiEngagementAdvisorPresenter(
    private val view: AiEngagementAdvisorContract.View,
    private val model: AiEngagementAdvisorModel,
) : AiEngagementAdvisorContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.headerBusinessName(context))
        view.setupNav(R.id.nav_home)
        view.setEngagementProgress(74)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
