package com.business.renvest.screens.loyalty

/**
 * Local ArrayList backing for the Loyalty reminders ListView.
 * Later you can populate [items] from an API response instead of seed data.
 */
class LoyaltyRemindersLocalStore {

    val items: ArrayList<LoyaltyReminderRow> = ArrayList(
        listOf(
            LoyaltyReminderRow(
                id = "seed_1",
                title = "Double points weekend",
                subtitle = "Sat–Sun • in-store only",
            ),
            LoyaltyReminderRow(
                id = "seed_2",
                title = "Birthday perk SMS",
                subtitle = "Send 3 days before birthday",
            ),
            LoyaltyReminderRow(
                id = "seed_3",
                title = "Stamp card bonus",
                subtitle = "Extra reward after 8 visits",
            ),
        ),
    )

    fun add(item: LoyaltyReminderRow) {
        items.add(item)
    }

    fun removeAt(index: Int): LoyaltyReminderRow? {
        if (index !in items.indices) return null
        return items.removeAt(index)
    }
}
