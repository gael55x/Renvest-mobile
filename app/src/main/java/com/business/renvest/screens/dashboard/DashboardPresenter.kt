package com.business.renvest.screens.dashboard

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.DashboardStats
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
            val stats = withContext(Dispatchers.IO) { model.dashboardStats() }
            withContext(Dispatchers.Main) {
                view.setupBottomNav(R.id.navHome, stats.counts.activityEvents)
                view.bindDashboardMetrics(buildBindModel(context, stats))
            }
        }
    }

    private fun buildBindModel(context: Context, stats: DashboardStats): DashboardBindModel {
        val counts = stats.counts
        val customers = counts.customers.toString()
        val visitsWeek = context.getString(R.string.dashboard_visits_this_week_format, stats.visitsThisWeek)
        val nearReward = stats.customersNearReward.toString()
        val (aiTitle, aiBody) = if (counts.totalRows() == 0) {
            context.getString(R.string.dashboard_advisor_card_title) to
                context.getString(R.string.dashboard_advisor_card_empty)
        } else {
            context.getString(R.string.dashboard_advisor_card_title) to
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
            revenueValue = stats.visitsThisWeek.toString(),
            revenueSubline = context.getString(R.string.dashboard_hero_visits_week_label),
            visitsValue = nearReward,
            membersValue = customers,
            returnValue = stats.customersReadyForReward.toString(),
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
        view.navigateToActivityFeed()
    }

    override fun onPerfViewReportClicked() {
        view.navigateToActivityFeed()
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
