package com.business.renvest.screens.activityfeed

import android.content.Context
import androidx.annotation.IdRes

interface ActivityFeedContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int)
        fun bindHeroMetrics(events: String, customers: String, promotions: String)
        fun bindActivityRows(items: List<ActivityEventRowUi>)
        fun setActivityEmptyVisible(visible: Boolean)
        fun showComingSoon()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onStubInteraction()
    }
}
