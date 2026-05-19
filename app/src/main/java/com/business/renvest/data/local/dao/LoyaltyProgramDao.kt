package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.business.renvest.data.local.entity.LoyaltyProgramEntity

@Dao
interface LoyaltyProgramDao {

    @Query("SELECT * FROM loyalty_programs ORDER BY updatedAt DESC")
    fun listAll(): List<LoyaltyProgramEntity>

    @Query("SELECT * FROM loyalty_programs WHERE id = :id LIMIT 1")
    fun findById(id: String): LoyaltyProgramEntity?

    @Insert
    fun insert(entity: LoyaltyProgramEntity)

    @Update
    fun update(entity: LoyaltyProgramEntity)

    @Query("DELETE FROM loyalty_programs WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM loyalty_programs")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM loyalty_programs")
    fun count(): Int
}
