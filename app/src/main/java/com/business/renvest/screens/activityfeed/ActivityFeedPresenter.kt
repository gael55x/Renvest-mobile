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
        val counts = model.localDataCounts()
        view.bindHeroMetrics(
            events = counts.activityEvents.toString(),
            customers = counts.customers.toString(),
            promotions = counts.promotions.toString(),
        )
        val rows = model.loadEvents()
        view.bindActivityRows(rows)
        view.setActivityEmptyVisible(rows.isEmpty())
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
