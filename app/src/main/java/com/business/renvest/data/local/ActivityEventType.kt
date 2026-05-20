package com.business.renvest.data.local

import com.business.renvest.screens.activityfeed.ActivityLogType

/** Stored in [com.business.renvest.data.local.entity.ActivityEventEntity.eventType]. */
object ActivityEventType {
    const val VISIT = "VISIT"
    const val POINTS = "POINTS"
    const val REWARD = "REWARD"
    const val CUSTOM = "CUSTOM"
    const val SYSTEM = "SYSTEM"

    fun fromLogType(type: ActivityLogType): String = when (type) {
        ActivityLogType.VISIT -> VISIT
        ActivityLogType.POINTS -> POINTS
        ActivityLogType.REWARD -> REWARD
        ActivityLogType.CUSTOM -> CUSTOM
    }

    fun isVisit(eventType: String, title: String): Boolean =
        eventType == VISIT || title.lowercase().contains("visit")
}
