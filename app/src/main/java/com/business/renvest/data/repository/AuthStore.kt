package com.business.renvest.data.repository

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.RenvestResult

class AuthStore {

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGGED_IN, false)

    fun getBusinessName(context: Context): String =
        prefs(context).getString(KEY_BUSINESS_NAME, "").orEmpty()

    fun getEmail(context: Context): String =
        prefs(context).getString(KEY_EMAIL, "").orEmpty()

    fun signInWithEmail(context: Context, email: String): RenvestResult<Unit> {
        prefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email.trim())
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun signUp(context: Context, businessName: String, email: String): RenvestResult<Unit> {
        prefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_BUSINESS_NAME, businessName.trim())
            .putString(KEY_EMAIL, email.trim())
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun clearSession(context: Context): RenvestResult<Unit> {
        prefs(context).edit().clear().apply()
        return RenvestResult.Ok(Unit)
    }

    fun businessDisplayName(context: Context): String {
        val stored = getBusinessName(context).trim()
        return if (stored.isNotEmpty()) stored else context.getString(R.string.default_business_display)
    }

    companion object {
        private const val PREFS_NAME = "renvest_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_BUSINESS_NAME = "business_name"
        private const val KEY_EMAIL = "email"
    }
}
