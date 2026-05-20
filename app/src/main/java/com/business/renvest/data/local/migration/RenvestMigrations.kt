package com.business.renvest.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS loyalty_programs (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                visitsRequired INTEGER NOT NULL,
                rewardDescription TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            "ALTER TABLE activity_events ADD COLUMN customerId TEXT DEFAULT NULL",
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE activity_events ADD COLUMN eventType TEXT NOT NULL DEFAULT 'CUSTOM'",
        )
        db.execSQL(
            """
            UPDATE activity_events SET eventType = 'VISIT'
            WHERE lower(title) LIKE '%visit%'
            """.trimIndent(),
        )
        db.execSQL(
            """
            UPDATE activity_events SET eventType = 'REWARD'
            WHERE lower(title) LIKE '%reward%'
            """.trimIndent(),
        )
        db.execSQL(
            """
            UPDATE activity_events SET eventType = 'POINTS'
            WHERE lower(title) LIKE '%point%'
            """.trimIndent(),
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE promotions ADD COLUMN enrolledCount INTEGER NOT NULL DEFAULT 0",
        )
        db.execSQL(
            "ALTER TABLE promotions ADD COLUMN redeemedCount INTEGER NOT NULL DEFAULT 0",
        )
    }
}
