package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthRepository
import com.business.renvest.data.repository.businessDisplayName

class ActivityFeedPresenter(
    private val view: ActivityFeedContract.View,
    private val authRepository: AuthRepository,
) : ActivityFeedContract.Presenter {

    override fun onViewReady(context: Context) {
        view.setHeaderBusinessName(authRepository.businessDisplayName(context))
        view.setupNav(R.id.nav_activity)
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
