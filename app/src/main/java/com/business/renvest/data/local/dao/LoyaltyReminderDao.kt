package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.business.renvest.data.local.entity.LoyaltyReminderEntity

@Dao
interface LoyaltyReminderDao {

    @Query("SELECT * FROM loyalty_reminders ORDER BY updatedAt DESC")
    fun listAll(): List<LoyaltyReminderEntity>

    @Insert
    fun insert(entity: LoyaltyReminderEntity)

    @Query("DELETE FROM loyalty_reminders WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("SELECT COUNT(*) FROM loyalty_reminders")
    fun count(): Int
}
