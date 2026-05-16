package com.business.renvest.screens.dashboard

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.LocalDataCounts
import java.util.Calendar

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val model: DashboardModel,
) : DashboardContract.Presenter {

    override fun onViewReady(context: Context) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        view.setGreeting(context.getString(DashboardModel.greetingStringResForHour(hour)))
        view.setBusinessName(model.businessDisplayName(context))
        val counts = model.localDataCounts()
        view.bindDashboardMetrics(buildBindModel(context, counts))
    }

    private fun buildBindModel(context: Context, counts: LocalDataCounts): DashboardBindModel {
        val notRecorded = context.getString(R.string.metric_not_recorded)
        val emDash = context.getString(R.string.metric_em_dash)
        val customers = counts.customers.toString()
        val (aiTitle, aiBody) = if (counts.totalRows() == 0) {
            context.getString(R.string.dashboard_ai_insight_placeholder_title) to
                context.getString(R.string.dashboard_ai_insight_placeholder_body)
        } else {
            context.getString(R.string.dashboard_ai_insight_placeholder_title) to
                context.getString(
                    R.string.dashboard_ai_insight_summary_format,
                    counts.customers,
                    counts.promotions,
                    counts.loyaltyReminders,
                    counts.activityEvents,
                )
        }
        return DashboardBindModel(
            avatarInitials = model.initialsFromBusiness(context),
            revenueValue = emDash,
            revenueSubline = notRecorded,
            visitsValue = emDash,
            membersValue = customers,
            returnValue = notRecorded,
            perfMembers = customers,
            perfMembersTrendVisible = false,
            perfRating = notRecorded,
            perfTicket = notRecorded,
            perfTicketTrendVisible = false,
            perfChurn = notRecorded,
            aiTitle = aiTitle,
            aiBody = aiBody,
        )
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
