package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.DashboardStats
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.dashboardStats
import com.business.renvest.data.local.primaryLoyaltyRule

data class AdvisorInsight(
    val title: String,
    val body: String,
)

fun RenvestDatabase.buildAdvisorInsights(context: Context): AdvisorInsight {
    val stats = dashboardStats()
    val counts = stats.counts
    if (counts.totalRows() == 0) {
        return AdvisorInsight(
            title = context.getString(R.string.ai_advisor_local_title_not_enough),
            body = context.getString(R.string.ai_advisor_local_body_not_enough),
        )
    }
    if (primaryLoyaltyRule() == null) {
        return AdvisorInsight(
            title = context.getString(R.string.ai_advisor_insight_no_program_title),
            body = context.getString(R.string.ai_advisor_insight_no_program_body),
        )
    }
    if (stats.visitsThisWeek == 0) {
        return AdvisorInsight(
            title = context.getString(R.string.ai_advisor_insight_no_visits_title),
            body = context.getString(R.string.ai_advisor_insight_no_visits_body),
        )
    }
    if (stats.customersNearReward > 0) {
        return AdvisorInsight(
            title = context.getString(R.string.ai_advisor_insight_near_reward_title),
            body = context.getString(
                R.string.ai_advisor_insight_near_reward_body,
                stats.customersNearReward,
            ),
        )
    }
    return AdvisorInsight(
        title = context.getString(R.string.ai_advisor_local_title_summary),
        body = context.getString(
            R.string.dashboard_ai_insight_summary_format,
            counts.customers,
            counts.promotions,
            counts.loyaltyPrograms,
            counts.loyaltyReminders,
            counts.activityEvents,
        ),
    )
}
