package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore

class ActivityFeedPresenter(
    private val view: ActivityFeedContract.View,
    private val authStore: AuthStore,
) : ActivityFeedContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authStore.businessDisplayName(context))
        view.setupNav(R.id.navActivity)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
