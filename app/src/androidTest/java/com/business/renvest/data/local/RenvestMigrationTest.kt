package com.business.renvest.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.business.renvest.data.local.migration.MIGRATION_1_2
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
}
