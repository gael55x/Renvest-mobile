package com.business.renvest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promotions")
data class PromotionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val reward: String,
    val expiry: String,
    val enrolledSummary: String,
    val usageSummary: String,
    val enrolledCount: Int,
    val redeemedCount: Int,
    val progressPercent: Int,
    /** Stored name of [com.business.renvest.screens.promotions.PromotionStatus] */
    val status: String,
    val useGiftIcon: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)
