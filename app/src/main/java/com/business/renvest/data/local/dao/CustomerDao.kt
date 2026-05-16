package com.business.renvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.business.renvest.data.local.entity.CustomerEntity

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers ORDER BY displayName COLLATE NOCASE ASC")
    fun listAll(): List<CustomerEntity>

    @Insert
    fun insert(entity: CustomerEntity)

    @Query("SELECT COUNT(*) FROM customers")
    fun count(): Int
}
