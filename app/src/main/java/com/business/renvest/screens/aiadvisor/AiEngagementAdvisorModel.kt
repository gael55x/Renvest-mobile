package com.business.renvest.screens.aiadvisor

import android.content.Context
import com.business.renvest.data.repository.AuthStore

class AiEngagementAdvisorModel(private val authStore: AuthStore) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    /** Placeholder until engagement metrics come from storage or API */
    fun demoEngagementProgressPercent(): Int = 74
}
