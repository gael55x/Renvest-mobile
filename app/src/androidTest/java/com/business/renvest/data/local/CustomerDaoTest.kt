package com.business.renvest.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.business.renvest.data.local.entity.CustomerEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class CustomerDaoTest {

    private lateinit var db: RenvestDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RenvestDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndCountCustomer() {
        val now = System.currentTimeMillis()
        db.customerDao().insert(
            CustomerEntity(
                id = UUID.randomUUID().toString(),
                displayName = "Test Customer",
                createdAt = now,
                updatedAt = now,
            ),
        )
        assertEquals(1, db.customerDao().count())
        assertEquals("Test Customer", db.customerDao().listAll().first().displayName)
    }
}
