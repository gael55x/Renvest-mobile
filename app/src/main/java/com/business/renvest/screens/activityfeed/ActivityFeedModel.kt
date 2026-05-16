package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore

class ActivityFeedModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadEvents(): List<ActivityEventRowUi> =
        db.activityEventDao().listAll().map {
            ActivityEventRowUi(id = it.id, title = it.title, subtitle = it.subtitle)
        }
}
