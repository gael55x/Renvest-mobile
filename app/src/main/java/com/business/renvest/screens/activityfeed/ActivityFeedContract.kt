package com.business.renvest.screens.activityfeed

import android.content.Context
import androidx.annotation.IdRes
import java.io.File

interface ActivityFeedContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int, activityBadgeCount: Int)
        fun bindHeroMetrics(events: String, customers: String, promotions: String)
        fun bindActivityRows(items: List<ActivityEventRowUi>)
        fun bindActionsCount(label: String)
        fun setActivityEmptyVisible(visible: Boolean)
        fun showToast(message: String)
        fun showAddActivityEventDialog(
            customers: List<Pair<String, String>>,
            onSubmit: (ActivityLogType, String, String, String?) -> Unit,
        )
        fun showDeleteActivityConfirm(title: String, onConfirm: () -> Unit)
        fun showDateRangeDialog(todayOnly: Boolean, onSelected: (Boolean) -> Unit)
        fun shareExportFile(file: File)
        fun restoreSearchQuery(query: String)
        fun selectCategory(category: ActivityFeedCategory)
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onLogEventClicked(context: Context)
        fun onLogEventSubmitted(
            context: Context,
            type: ActivityLogType,
            customTitle: String,
            subtitle: String,
            customerId: String?,
        )
        fun onActivityLongPressed(context: Context, row: ActivityEventRowUi)
        fun onSearchQueryChanged(context: Context, query: String)
        fun onCategorySelected(context: Context, category: ActivityFeedCategory)
        fun onCalendarClicked(context: Context)
        fun onExportClicked(context: Context)
        fun onDateRangeSelected(context: Context, todayOnly: Boolean)
    }
}
