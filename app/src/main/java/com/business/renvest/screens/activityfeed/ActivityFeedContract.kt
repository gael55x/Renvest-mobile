package com.business.renvest.screens.activityfeed

import android.content.Context
import androidx.annotation.IdRes
import com.business.renvest.R

interface ActivityFeedContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int)
        fun bindHeroMetrics(events: String, customers: String, promotions: String)
        fun bindActivityRows(items: List<ActivityEventRowUi>)
        fun setActivityEmptyVisible(visible: Boolean)
        fun showToast(message: String)
        fun showAddActivityEventDialog(onSubmit: (String, String) -> Unit)
        fun showDeleteActivityConfirm(title: String, onConfirm: () -> Unit)
        fun showComingSoon()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onLogEventClicked(context: Context)
        fun onAddActivitySubmitted(context: Context, title: String, subtitle: String)
        fun onActivityLongPressed(context: Context, row: ActivityEventRowUi)
        fun onStubInteraction()
    }
}
