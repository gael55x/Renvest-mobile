package com.business.renvest.screens.profile

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.repository.AuthRepository
import com.business.renvest.data.repository.businessDisplayName

class ProfileModel(private val authRepository: AuthRepository) {

    fun getEmail(context: Context): String = authRepository.getEmail(context)

    fun businessDisplayName(context: Context): String = authRepository.businessDisplayName(context)

    fun clearSession(context: Context): RenvestResult<Unit> = authRepository.clearSession(context)

    fun initialsFromName(name: String): String {
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        return when {
            parts.isEmpty() -> "RV"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts.last().first()}".uppercase()
        }
    }
}
