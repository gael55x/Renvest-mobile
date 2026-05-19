package com.business.renvest.data.local

import com.business.renvest.data.local.entity.ActivityEventEntity
import java.util.UUID

fun RenvestDatabase.logActivity(
    title: String,
    subtitle: String = "",
    customerId: String? = null,
) {
    val now = System.currentTimeMillis()
    activityEventDao().insert(
        ActivityEventEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            subtitle = subtitle,
            customerId = customerId,
            createdAt = now,
        ),
    )
}
