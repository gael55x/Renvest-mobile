package com.business.renvest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_events")
data class ActivityEventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    /** Optional link to [CustomerEntity.id]. */
    val customerId: String?,
    /** [com.business.renvest.data.local.ActivityEventType] */
    val eventType: String,
    val createdAt: Long,
)
