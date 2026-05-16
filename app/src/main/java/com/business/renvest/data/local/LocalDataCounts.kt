package com.business.renvest.data.local

/** Aggregate row counts for dashboards and AI context (no invented KPIs). */
data class LocalDataCounts(
    val loyaltyReminders: Int,
    val promotions: Int,
    val promotionsActive: Int,
    val customers: Int,
    val activityEvents: Int,
) {
    fun totalRows(): Int = loyaltyReminders + promotions + customers + activityEvents
}

fun RenvestDatabase.localDataCounts(): LocalDataCounts {
    val promoDao = promotionDao()
    return LocalDataCounts(
        loyaltyReminders = loyaltyReminderDao().count(),
        promotions = promoDao.count(),
        promotionsActive = promoDao.countByStatus("Active"),
        customers = customerDao().count(),
        activityEvents = activityEventDao().count(),
    )
}
