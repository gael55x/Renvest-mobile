package com.business.renvest.screens.dashboard

import android.content.Context

interface DashboardContract {
    interface View {
        fun setGreeting(text: String)
        fun setBusinessName(text: String)
        fun showComingSoon()
        fun navigateToCustomers()
        fun navigateToLoyalty()
        fun navigateToPromotions()
        fun navigateToAiAdvisor()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onNotificationClicked()
        fun onPerfViewReportClicked()
        fun onPerfCellMembersClicked()
        fun onPerfCellRatingClicked()
        fun onPerfCellTicketClicked()
        fun onPerfCellChurnClicked()
        fun onCardAiInsightClicked()
    }
}
