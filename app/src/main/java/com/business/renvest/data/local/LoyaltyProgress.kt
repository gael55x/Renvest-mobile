package com.business.renvest.data.local

import com.business.renvest.data.local.entity.ActivityEventEntity
import com.business.renvest.data.local.entity.LoyaltyProgramEntity
import java.text.DateFormat
import java.util.Date
import java.util.Locale

data class LoyaltyRule(
    val visitsRequired: Int,
    val rewardDescription: String,
    val programName: String,
)

data class CustomerVisitProgress(
    val currentVisits: Int,
    val visitsRequired: Int,
    val nearReward: Boolean,
    val readyForReward: Boolean,
    val lastVisitLabel: String,
)

fun RenvestDatabase.primaryLoyaltyRule(): LoyaltyRule? {
    val program = loyaltyProgramDao().listAll().minByOrNull { it.createdAt } ?: return null
    return program.toRule()
}

fun RenvestDatabase.visitProgressForCustomer(customerId: String): CustomerVisitProgress {
    val rule = primaryLoyaltyRule()
    val visitsRequired = rule?.visitsRequired ?: 10
    val events = activityEventDao().listByCustomerId(customerId)
    val currentVisits = countVisitsSinceLastReward(events)
    val nearReward = currentVisits == visitsRequired - 1 && visitsRequired > 0
    val readyForReward = currentVisits >= visitsRequired && visitsRequired > 0
    val lastVisitAt = events.firstOrNull { ActivityEventType.isVisit(it.eventType, it.title) }?.createdAt
    val lastVisitLabel = if (lastVisitAt != null) {
        DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(Date(lastVisitAt))
    } else {
        ""
    }
    return CustomerVisitProgress(
        currentVisits = currentVisits.coerceAtMost(visitsRequired),
        visitsRequired = visitsRequired,
        nearReward = nearReward,
        readyForReward = readyForReward,
        lastVisitLabel = lastVisitLabel,
    )
}

fun RenvestDatabase.countCustomersNearReward(): Int =
    customerDao().listAll().count { visitProgressForCustomer(it.id).nearReward }

fun RenvestDatabase.countCustomersReadyForReward(): Int =
    customerDao().listAll().count { visitProgressForCustomer(it.id).readyForReward }

private fun LoyaltyProgramEntity.toRule(): LoyaltyRule =
    LoyaltyRule(
        visitsRequired = visitsRequired,
        rewardDescription = rewardDescription,
        programName = name,
    )

private fun countVisitsSinceLastReward(events: List<ActivityEventEntity>): Int {
    var count = 0
    for (event in events.sortedByDescending { it.createdAt }) {
        when {
            event.eventType == ActivityEventType.REWARD ||
                event.title.lowercase().contains("reward") -> return count
            ActivityEventType.isVisit(event.eventType, event.title) -> count++
        }
    }
    return count
}
