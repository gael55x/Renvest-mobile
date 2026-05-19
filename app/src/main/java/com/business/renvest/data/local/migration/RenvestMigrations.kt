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
