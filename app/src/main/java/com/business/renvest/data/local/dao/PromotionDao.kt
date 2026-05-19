package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.business.renvest.data.local.entity.PromotionEntity

@Dao
interface PromotionDao {

    @Query("SELECT * FROM promotions ORDER BY updatedAt DESC")
    fun listAll(): List<PromotionEntity>

    @Insert
    fun insert(entity: PromotionEntity)

    @Update
    fun update(entity: PromotionEntity)

    @Query("UPDATE promotions SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    fun updateStatus(id: String, status: String, updatedAt: Long): Int

    @Query("DELETE FROM promotions WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM promotions")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM promotions")
    fun count(): Int

    @Query("SELECT COUNT(*) FROM promotions WHERE status = :status")
    fun countByStatus(status: String): Int
}
