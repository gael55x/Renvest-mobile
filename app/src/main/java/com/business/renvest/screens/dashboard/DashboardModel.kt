package com.business.renvest.screens.dashboard

import android.content.Context
import androidx.annotation.StringRes
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore

class DashboardModel(private val authStore: AuthStore) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    companion object {
        @StringRes
        fun greetingStringResForHour(hour: Int): Int = when {
            hour < 12 -> R.string.greeting_morning
            hour < 17 -> R.string.greeting_afternoon
            else -> R.string.greeting_evening
        }
    }
}
