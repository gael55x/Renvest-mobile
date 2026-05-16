package com.business.renvest.app

import android.app.Application
import android.util.Log
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.repository.AuthStore

class RenvestApp : Application() {

    val authStore: AuthStore by lazy { AuthStore() }

    val database: RenvestDatabase by lazy { RenvestDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    companion object {
        private const val TAG = "RenvestApp"
    }
}
