package com.business.renvest.screens.dashboard

import android.content.Context
import com.business.renvest.data.repository.AuthStore

class DashboardModel(private val authStore: AuthStore) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)
}
