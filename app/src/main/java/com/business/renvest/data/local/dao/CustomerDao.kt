package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.business.renvest.data.local.entity.CustomerEntity

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers ORDER BY displayName COLLATE NOCASE ASC")
    fun listAll(): List<CustomerEntity>

    @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
    fun findById(id: String): CustomerEntity?

    @Insert
    fun insert(entity: CustomerEntity)

    @Query(
        """
        UPDATE customers SET displayName = :displayName, updatedAt = :updatedAt
        WHERE id = :id
        """,
    )
    fun updateName(id: String, displayName: String, updatedAt: Long): Int

    @Query("DELETE FROM customers WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM customers")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM customers")
    fun count(): Int
}
