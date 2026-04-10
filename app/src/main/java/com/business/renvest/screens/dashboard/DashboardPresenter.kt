package com.business.renvest.screens.dashboard

import android.content.Context
import java.util.Calendar

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val model: DashboardModel,
) : DashboardContract.Presenter {

    override fun onViewReady(context: Context) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        view.setGreeting(context.getString(model.greetingResForHour(hour)))
        view.setBusinessName(model.businessDisplayName(context))
    }

    override fun onNotificationClicked() {
        view.showComingSoon()
    }

    override fun onPerfViewReportClicked() {
        view.showComingSoon()
    }

    override fun onPerfCellMembersClicked() {
        view.navigateToCustomers()
    }

    override fun onPerfCellRatingClicked() {
        view.navigateToLoyalty()
    }

    override fun onPerfCellTicketClicked() {
        view.navigateToPromotions()
    }

    override fun onPerfCellChurnClicked() {
        view.showComingSoon()
    }

    override fun onCardAiInsightClicked() {
        view.navigateToAiAdvisor()
    }
}
