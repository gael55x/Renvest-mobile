package com.business.renvest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.business.renvest.data.local.dao.ActivityEventDao
import com.business.renvest.data.local.dao.CustomerDao
import com.business.renvest.data.local.dao.LoyaltyReminderDao
import com.business.renvest.data.local.dao.PromotionDao
import com.business.renvest.data.local.entity.ActivityEventEntity
import com.business.renvest.data.local.entity.CustomerEntity
import com.business.renvest.data.local.entity.LoyaltyReminderEntity
import com.business.renvest.data.local.entity.PromotionEntity

/**
 * Local SQLite source of truth. Uses main-thread queries for the synchronous MVP presenters;
 * move work off the UI thread when the app grows.
 */
@Database(
    entities = [
        LoyaltyReminderEntity::class,
        PromotionEntity::class,
        CustomerEntity::class,
        ActivityEventEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class RenvestDatabase : RoomDatabase() {

    abstract fun loyaltyReminderDao(): LoyaltyReminderDao
    abstract fun promotionDao(): PromotionDao
    abstract fun customerDao(): CustomerDao
    abstract fun activityEventDao(): ActivityEventDao

    companion object {
        @Volatile
        private var instance: RenvestDatabase? = null

        fun getInstance(context: Context): RenvestDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RenvestDatabase::class.java,
                    "renvest.db",
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { instance = it }
            }
    }
}
