package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.LocalDataExporter
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.ActivityEventEntity
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore
import java.io.File
import java.util.Calendar
import java.util.UUID

class ActivityFeedModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadCustomersForPicker(): List<Pair<String, String>> =
        db.customerDao().listAll().map { it.id to it.displayName }

    fun loadEvents(
        searchQuery: String,
        category: ActivityFeedCategory,
        todayOnly: Boolean,
    ): List<ActivityEventRowUi> {
        val query = searchQuery.trim().lowercase()
        return db.activityEventDao().listAll()
            .asSequence()
            .filter { !todayOnly || isToday(it.createdAt) }
            .filter { matchesCategory(it, category) }
            .filter { entity ->
                if (query.isEmpty()) true
                else {
                    val haystack = "${entity.title} ${entity.subtitle} ${customerName(entity)}".lowercase()
                    haystack.contains(query)
                }
            }
            .map { entity ->
                ActivityEventRowUi(
                    id = entity.id,
                    title = entity.title,
                    subtitle = formatSubtitle(entity),
                    customerId = entity.customerId,
                )
            }
            .toList()
    }

    fun resolveTitle(type: ActivityLogType, customTitle: String): String {
        val custom = customTitle.trim()
        return when (type) {
            ActivityLogType.VISIT -> "Visit logged"
            ActivityLogType.POINTS -> "Points earned"
            ActivityLogType.REWARD -> "Reward redeemed"
            ActivityLogType.CUSTOM -> custom
        }
    }

    /** @return false if title is blank after trim */
    fun addEvent(title: String, subtitle: String, customerId: String?): Boolean {
        val t = title.trim()
        if (t.isEmpty()) return false
        val now = System.currentTimeMillis()
        db.activityEventDao().insert(
            ActivityEventEntity(
                id = UUID.randomUUID().toString(),
                title = t,
                subtitle = subtitle.trim(),
                customerId = customerId?.takeIf { it.isNotBlank() },
                createdAt = now,
            ),
        )
        return true
    }

    fun removeEvent(id: String) {
        db.activityEventDao().deleteById(id)
    }

    fun exportLocalData(context: Context): File = LocalDataExporter(db).exportToCacheFile(context)

    fun markOnboardingActivityStep(context: Context) {
        authStore.markOnboardingStep(context, AuthStore.STEP_ACTIVITY)
    }

    private fun matchesCategory(entity: ActivityEventEntity, category: ActivityFeedCategory): Boolean {
        if (category == ActivityFeedCategory.ALL) return true
        val haystack = "${entity.title} ${entity.subtitle}".lowercase()
        return when (category) {
            ActivityFeedCategory.POINTS -> haystack.contains("point")
            ActivityFeedCategory.REWARD -> haystack.contains("reward")
            ActivityFeedCategory.VISIT -> haystack.contains("visit")
            ActivityFeedCategory.ALL -> true
        }
    }

    private fun isToday(timestamp: Long): Boolean {
        val eventCal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val today = Calendar.getInstance()
        return eventCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            eventCal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    }

    private fun customerName(entity: ActivityEventEntity): String =
        entity.customerId?.let { db.customerDao().findById(it)?.displayName }.orEmpty()

    private fun formatSubtitle(entity: ActivityEventEntity): String {
        val base = entity.subtitle
        val customerId = entity.customerId ?: return base
        val name = db.customerDao().findById(customerId)?.displayName
        return if (name != null && base.isNotEmpty()) "$name · $base"
        else if (name != null) name
        else base
    }
}
