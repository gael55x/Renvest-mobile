package com.business.renvest.screens.dashboard

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.LocalDataCounts
import java.util.Calendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val model: DashboardModel,
    private val scope: CoroutineScope,
) : DashboardContract.Presenter {

    override fun onViewReady(context: Context) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        view.setGreeting(context.getString(DashboardModel.greetingStringResForHour(hour)))
        view.setBusinessName(model.businessDisplayName(context))
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            withContext(Dispatchers.Main) {
                view.bindDashboardMetrics(buildBindModel(context, counts))
            }
        }
    }

    private fun buildBindModel(context: Context, counts: LocalDataCounts): DashboardBindModel {
        val notRecorded = context.getString(R.string.metric_not_recorded)
        val emDash = context.getString(R.string.metric_em_dash)
        val customers = counts.customers.toString()
        val (aiTitle, aiBody) = if (counts.totalRows() == 0) {
            context.getString(R.string.dashboard_local_insights_title) to
                context.getString(R.string.dashboard_local_insights_empty_body)
        } else {
            context.getString(R.string.dashboard_local_insights_title) to
                context.getString(
                    R.string.dashboard_ai_insight_summary_format,
                    counts.customers,
                    counts.promotions,
                    counts.loyaltyPrograms,
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
            perfLoyaltyPrograms = counts.loyaltyPrograms.toString(),
            perfPromotions = counts.promotionsActive.toString(),
            perfPromotionsTrendVisible = false,
            perfActivityEvents = counts.activityEvents.toString(),
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

    override fun onPerfCellLoyaltyClicked() {
        view.navigateToLoyalty()
    }

    override fun onPerfCellPromotionsClicked() {
        view.navigateToPromotions()
    }

    override fun onPerfCellActivityClicked() {
        view.navigateToActivityFeed()
    }

    override fun onCardLocalInsightsClicked() {
        view.navigateToLocalInsights()
    }
}
