package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.business.renvest.data.local.entity.PromotionEntity

@Dao
interface PromotionDao {

    @Query("SELECT * FROM promotions ORDER BY updatedAt DESC")
    fun listAll(): List<PromotionEntity>

    @Insert
    fun insert(entity: PromotionEntity)

    @Query("SELECT COUNT(*) FROM promotions")
    fun count(): Int

    @Query("SELECT COUNT(*) FROM promotions WHERE status = :status")
    fun countByStatus(status: String): Int
}
