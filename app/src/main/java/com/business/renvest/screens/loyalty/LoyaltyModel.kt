package com.business.renvest.screens.loyalty

import androidx.annotation.StringRes
import com.business.renvest.R
import java.util.UUID

class LoyaltyModel(
    private val remindersStore: LoyaltyRemindersLocalStore = LoyaltyRemindersLocalStore(),
) {

    @StringRes
    fun stubScreenTitleRes(): Int = R.string.feature_loyalty_title

    fun remindersSnapshot(): ArrayList<LoyaltyReminderRow> = remindersStore.items

    fun reminderAt(index: Int): LoyaltyReminderRow? = remindersStore.items.getOrNull(index)

    fun addReminderFromTitle(raw: String): Boolean {
        val title = raw.trim()
        if (title.isEmpty()) return false
        remindersStore.add(
            LoyaltyReminderRow(
                id = UUID.randomUUID().toString(),
                title = title,
                subtitle = NEW_REMINDER_SUBTITLE,
            ),
        )
        return true
    }

    fun removeReminderAt(index: Int): LoyaltyReminderRow? = remindersStore.removeAt(index)

    companion object {
        private const val NEW_REMINDER_SUBTITLE = "Tap row for details • Long-press to remove"
    }
}
