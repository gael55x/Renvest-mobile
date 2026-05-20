package com.business.renvest.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.business.renvest.data.local.migration.MIGRATION_1_2
import com.business.renvest.data.local.migration.MIGRATION_2_3
import com.business.renvest.data.local.migration.MIGRATION_3_4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RenvestMigrationTest {

    private val testDb = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        RenvestDatabase::class.java,
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrate1To2_addsLoyaltyProgramsAndCustomerId() {
        helper.createDatabase(testDb, 1).apply {
            execSQL(
                """
                INSERT INTO customers (id, displayName, createdAt, updatedAt)
                VALUES ('c1', 'Ada', 1, 1)
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO activity_events (id, title, subtitle, createdAt)
                VALUES ('e1', 'Visit', '', 2)
                """.trimIndent(),
            )
            close()
        }
        helper.runMigrationsAndValidate(testDb, 2, true, MIGRATION_1_2)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.databaseBuilder(context, RenvestDatabase::class.java, testDb)
            .addMigrations(MIGRATION_1_2)
            .build()
        db.loyaltyProgramDao().count()
        db.close()
    }

    @Test
    fun migrate2To3_addsEventType() {
        helper.createDatabase(testDb, 2).apply {
            execSQL(
                """
                INSERT INTO activity_events (id, title, subtitle, customerId, createdAt)
                VALUES ('e1', 'Visit logged', '', NULL, 2)
                """.trimIndent(),
            )
            close()
        }
        helper.runMigrationsAndValidate(testDb, 3, true, MIGRATION_2_3)
    }

    @Test
    fun migrate3To4_addsPromotionCounts() {
        helper.createDatabase(testDb, 3).apply {
            execSQL(
                """
                INSERT INTO promotions (
                    id, title, reward, expiry, enrolledSummary, usageSummary,
                    progressPercent, status, useGiftIcon, createdAt, updatedAt
                ) VALUES (
                    'p1', 'Summer', 'Free drink', 'Dec 31', '', '',
                    0, 'Active', 0, 1, 1
                )
                """.trimIndent(),
            )
            close()
        }
        helper.runMigrationsAndValidate(testDb, 4, true, MIGRATION_3_4)
    }
}
