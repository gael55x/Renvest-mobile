package com.business.renvest.data.local

/** Aggregate row counts for dashboards and AI context (no invented KPIs). */
data class LocalDataCounts(
    val loyaltyPrograms: Int,
    val loyaltyReminders: Int,
    val promotions: Int,
    val promotionsActive: Int,
    val customers: Int,
    val activityEvents: Int,
) {
    fun totalRows(): Int =
        loyaltyPrograms + loyaltyReminders + promotions + customers + activityEvents
}

fun RenvestDatabase.localDataCounts(): LocalDataCounts {
    val promoDao = promotionDao()
    return LocalDataCounts(
        loyaltyPrograms = loyaltyProgramDao().count(),
        loyaltyReminders = loyaltyReminderDao().count(),
        promotions = promoDao.count(),
        promotionsActive = promoDao.countByStatus("Active"),
        customers = customerDao().count(),
        activityEvents = activityEventDao().count(),
    )
}
