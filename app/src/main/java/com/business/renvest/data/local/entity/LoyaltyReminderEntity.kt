package com.business.renvest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loyalty_reminders")
data class LoyaltyReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val createdAt: Long,
    val updatedAt: Long,
)
