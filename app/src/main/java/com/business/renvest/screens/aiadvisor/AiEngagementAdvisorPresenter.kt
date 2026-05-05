package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.R

class AiEngagementAdvisorPresenter(
    private val view: AiEngagementAdvisorContract.View,
    private val model: AiEngagementAdvisorModel,
) : AiEngagementAdvisorContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.businessDisplayName(context))
        view.setupNav(R.id.navHome)
        view.setEngagementProgress(model.demoEngagementProgressPercent())
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
