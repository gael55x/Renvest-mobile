package com.business.renvest.data.local

import com.business.renvest.data.local.dao.ActivityEventDao
import com.business.renvest.data.local.dao.CustomerDao
import com.business.renvest.data.local.dao.LoyaltyProgramDao
import com.business.renvest.data.local.dao.LoyaltyReminderDao
import com.business.renvest.data.local.dao.PromotionDao

/** Wipes all local business rows (session prefs are cleared separately). */
fun RenvestDatabase.clearAllBusinessData() {
    clearTable(customerDao())
    clearTable(promotionDao())
    clearTable(loyaltyReminderDao())
    clearTable(activityEventDao())
    clearTable(loyaltyProgramDao())
}

private inline fun <T> clearTable(dao: T) {
    when (dao) {
        is CustomerDao -> dao.deleteAll()
        is PromotionDao -> dao.deleteAll()
        is LoyaltyReminderDao -> dao.deleteAll()
        is ActivityEventDao -> dao.deleteAll()
        is LoyaltyProgramDao -> dao.deleteAll()
    }
}
