package com.business.renvest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val createdAt: Long,
    val updatedAt: Long,
)
