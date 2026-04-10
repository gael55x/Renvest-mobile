package com.business.renvest.app

import android.app.Application
import android.util.Log
import com.business.renvest.data.repository.AuthStore

class RenvestApp : Application() {

    val authStore: AuthStore by lazy { AuthStore() }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    companion object {
        private const val TAG = "RenvestApp"
    }
}
