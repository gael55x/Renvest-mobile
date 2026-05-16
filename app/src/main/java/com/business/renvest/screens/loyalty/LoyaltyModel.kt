package com.business.renvest.screens.loyalty

import androidx.annotation.StringRes
import com.business.renvest.R
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.LoyaltyReminderEntity
import java.util.UUID

class LoyaltyModel(
    private val db: RenvestDatabase,
) {

    private val dao get() = db.loyaltyReminderDao()

    @StringRes
    fun stubScreenTitleRes(): Int = R.string.feature_loyalty_title

    fun remindersSnapshot(): ArrayList<LoyaltyReminderRow> =
        ArrayList(dao.listAll().map { it.toRow() })

    fun reminderAt(index: Int): LoyaltyReminderRow? =
        dao.listAll().getOrNull(index)?.toRow()

    fun addReminderFromTitle(raw: String): Boolean {
        val title = raw.trim()
        if (title.isEmpty()) return false
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        dao.insert(
            LoyaltyReminderEntity(
                id = id,
                title = title,
                subtitle = NEW_REMINDER_SUBTITLE,
                createdAt = now,
                updatedAt = now,
            ),
        )
        return true
    }

    fun removeReminderAt(index: Int): LoyaltyReminderRow? {
        val entity = dao.listAll().getOrNull(index) ?: return null
        dao.deleteById(entity.id)
        return entity.toRow()
    }

    private fun LoyaltyReminderEntity.toRow(): LoyaltyReminderRow =
        LoyaltyReminderRow(id = id, title = title, subtitle = subtitle)

    companion object {
        private const val NEW_REMINDER_SUBTITLE = "Tap row for details • Long-press to remove"
    }
}
