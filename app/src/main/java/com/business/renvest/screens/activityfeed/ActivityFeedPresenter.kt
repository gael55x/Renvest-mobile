package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.R

class ActivityFeedPresenter(
    private val view: ActivityFeedContract.View,
    private val model: ActivityFeedModel,
) : ActivityFeedContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(model.businessDisplayName(context))
        view.setupNav(R.id.navActivity)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
