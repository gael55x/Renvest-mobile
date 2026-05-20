package com.business.renvest.screens.dashboard

import android.content.Context
import androidx.annotation.IdRes

interface DashboardContract {
    interface View {
        fun setGreeting(text: String)
        fun setBusinessName(text: String)
        fun setupBottomNav(@IdRes selectedItemId: Int, activityBadgeCount: Int)
        fun bindDashboardMetrics(model: DashboardBindModel)
        fun showComingSoon()
        fun navigateToCustomers()
        fun navigateToLoyalty()
        fun navigateToPromotions()
        fun navigateToActivityFeed()
        fun navigateToLocalInsights()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onNotificationClicked()
        fun onPerfViewReportClicked()
        fun onPerfCellMembersClicked()
        fun onPerfCellLoyaltyClicked()
        fun onPerfCellPromotionsClicked()
        fun onPerfCellActivityClicked()
        fun onCardLocalInsightsClicked()
    }
}
