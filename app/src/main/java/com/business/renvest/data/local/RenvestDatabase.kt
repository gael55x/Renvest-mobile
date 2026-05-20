package com.business.renvest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.business.renvest.data.local.dao.ActivityEventDao
import com.business.renvest.data.local.dao.CustomerDao
import com.business.renvest.data.local.dao.LoyaltyProgramDao
import com.business.renvest.data.local.dao.LoyaltyReminderDao
import com.business.renvest.data.local.dao.PromotionDao
import com.business.renvest.data.local.entity.ActivityEventEntity
import com.business.renvest.data.local.entity.CustomerEntity
import com.business.renvest.data.local.entity.LoyaltyProgramEntity
import com.business.renvest.data.local.entity.LoyaltyReminderEntity
import com.business.renvest.data.local.entity.PromotionEntity
import com.business.renvest.data.local.migration.MIGRATION_1_2
import com.business.renvest.data.local.migration.MIGRATION_2_3
import com.business.renvest.data.local.migration.MIGRATION_3_4

@Database(
    entities = [
        LoyaltyReminderEntity::class,
        LoyaltyProgramEntity::class,
        PromotionEntity::class,
        CustomerEntity::class,
        ActivityEventEntity::class,
    ],
    version = 4,
    exportSchema = false,
)
abstract class RenvestDatabase : RoomDatabase() {

    abstract fun loyaltyReminderDao(): LoyaltyReminderDao
    abstract fun loyaltyProgramDao(): LoyaltyProgramDao
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                    .also { instance = it }
            }
    }
}
