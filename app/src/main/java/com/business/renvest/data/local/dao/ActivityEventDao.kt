package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.business.renvest.data.local.entity.ActivityEventEntity

@Dao
interface ActivityEventDao {

    @Query("SELECT * FROM activity_events ORDER BY createdAt DESC")
    fun listAll(): List<ActivityEventEntity>

    @Query("SELECT * FROM activity_events WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun listByCustomerId(customerId: String): List<ActivityEventEntity>

    @Insert
    fun insert(entity: ActivityEventEntity)

    @Query("DELETE FROM activity_events WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM activity_events")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM activity_events")
    fun count(): Int
}
