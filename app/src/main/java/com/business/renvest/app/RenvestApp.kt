package com.business.renvest.app

import android.app.Application
import android.util.Log
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.repository.AuthStore
import com.business.renvest.data.repository.SessionPreferences
import java.util.concurrent.Executors

class RenvestApp : Application() {

    val sessionPreferences: SessionPreferences by lazy { SessionPreferences(this) }

    val authStore: AuthStore by lazy { AuthStore(sessionPreferences) }

    val database: RenvestDatabase by lazy { RenvestDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        // Warm up encrypted prefs off the main thread so login tap does not freeze the UI.
        Executors.newSingleThreadExecutor().execute {
            try {
                sessionPreferences.prefs
            } catch (e: Exception) {
                Log.w(TAG, "Session prefs warmup failed", e)
            }
        }
    }

    companion object {
        private const val TAG = "RenvestApp"
    }
}
