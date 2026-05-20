package com.business.renvest.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Encrypted session storage for identity and onboarding flags.
 * Falls back to private SharedPreferences if encryption is unavailable.
 * Use a single instance from [com.business.renvest.app.RenvestApp] to avoid repeated slow init.
 */
class SessionPreferences(context: Context) {

    private val appContext = context.applicationContext

    @Volatile
    private var cachedPrefs: SharedPreferences? = null

    val prefs: SharedPreferences
        get() {
            cachedPrefs?.let { return it }
            return synchronized(this) {
                cachedPrefs ?: createPrefs().also { cachedPrefs = it }
            }
        }

    private fun createPrefs(): SharedPreferences =
        try {
            val masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedSharedPreferences.create(
                appContext,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        } catch (e: Exception) {
            Log.w(TAG, "Encrypted prefs unavailable, using private prefs", e)
            appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

    companion object {
        private const val TAG = "SessionPreferences"
        const val PREFS_NAME = "renvest_session"
    }
}
