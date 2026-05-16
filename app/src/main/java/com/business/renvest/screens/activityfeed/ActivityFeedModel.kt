package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.ActivityEventEntity
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore
import java.util.UUID

class ActivityFeedModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadEvents(): List<ActivityEventRowUi> =
        db.activityEventDao().listAll().map {
            ActivityEventRowUi(id = it.id, title = it.title, subtitle = it.subtitle)
        }

    /** @return false if title is blank after trim */
    fun addEvent(title: String, subtitle: String): Boolean {
        val t = title.trim()
        if (t.isEmpty()) return false
        val now = System.currentTimeMillis()
        db.activityEventDao().insert(
            ActivityEventEntity(
                id = UUID.randomUUID().toString(),
                title = t,
                subtitle = subtitle.trim(),
                createdAt = now,
            ),
        )
        return true
    }

    fun removeEvent(id: String) {
        db.activityEventDao().deleteById(id)
    }
}
