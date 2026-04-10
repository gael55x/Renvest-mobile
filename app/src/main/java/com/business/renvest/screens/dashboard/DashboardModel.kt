package com.business.renvest.screens.dashboard

import android.content.Context
import androidx.annotation.StringRes
import com.business.renvest.R
import com.business.renvest.data.repository.AuthRepository

class DashboardModel(private val authRepository: AuthRepository) {

    fun businessDisplayName(context: Context): String {
        val stored = authRepository.getBusinessName(context).trim()
        return if (stored.isNotEmpty()) stored else context.getString(R.string.default_business_display)
    }

    @StringRes
    fun greetingResForHour(hour: Int): Int = when {
        hour < 12 -> R.string.greeting_morning
        hour < 17 -> R.string.greeting_afternoon
        else -> R.string.greeting_evening
    }
}
