package com.business.renvest.data.local

import java.util.Calendar

data class DashboardStats(
    val counts: LocalDataCounts,
    val visitsThisWeek: Int,
    val customersNearReward: Int,
    val customersReadyForReward: Int,
)

fun RenvestDatabase.dashboardStats(): DashboardStats {
    val counts = localDataCounts()
    return DashboardStats(
        counts = counts,
        visitsThisWeek = countVisitsThisWeek(),
        customersNearReward = countCustomersNearReward(),
        customersReadyForReward = countCustomersReadyForReward(),
    )
}

private fun RenvestDatabase.countVisitsThisWeek(): Int {
    val weekStart = startOfWeekMillis()
    return activityEventDao().listAll().count { event ->
        event.createdAt >= weekStart && ActivityEventType.isVisit(event.eventType, event.title)
    }
}

private fun startOfWeekMillis(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
