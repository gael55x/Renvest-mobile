package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.data.repository.AuthStore

class ActivityFeedModel(private val authStore: AuthStore) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)
}
