package com.business.renvest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loyalty_programs")
data class LoyaltyProgramEntity(
    @PrimaryKey val id: String,
    val name: String,
    val visitsRequired: Int,
    val rewardDescription: String,
    val createdAt: Long,
    val updatedAt: Long,
)
