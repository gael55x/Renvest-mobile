package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.business.renvest.data.local.entity.ActivityEventEntity

@Dao
interface ActivityEventDao {

    @Query("SELECT * FROM activity_events ORDER BY createdAt DESC")
    fun listAll(): List<ActivityEventEntity>

    @Insert
    fun insert(entity: ActivityEventEntity)

    @Query("DELETE FROM activity_events WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("SELECT COUNT(*) FROM activity_events")
    fun count(): Int
}
