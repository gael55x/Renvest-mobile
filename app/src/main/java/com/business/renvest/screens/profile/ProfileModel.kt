package com.business.renvest.screens.profile

import android.content.Context
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.repository.AuthStore

class ProfileModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun getEmail(context: Context): String = authStore.getEmail(context)

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun clearSession(context: Context): RenvestResult<Unit> = authStore.clearSession(context)

    fun initialsFromName(name: String): String {
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        return when {
            parts.isEmpty() -> "RV"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts.last().first()}".uppercase()
        }
    }
}
