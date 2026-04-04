package com.business.renvest.data.local

import android.content.Context

class SessionLocalDataSourceImpl : SessionLocalDataSource {

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun isLoggedIn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGGED_IN, false)

    override fun getBusinessName(context: Context): String =
        prefs(context).getString(KEY_BUSINESS_NAME, "").orEmpty()

    override fun getEmail(context: Context): String =
        prefs(context).getString(KEY_EMAIL, "").orEmpty()

    override fun signInWithEmail(context: Context, email: String) {
        prefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email.trim())
            .apply()
    }

    override fun signUp(context: Context, businessName: String, email: String) {
        prefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_BUSINESS_NAME, businessName.trim())
            .putString(KEY_EMAIL, email.trim())
            .apply()
    }

    override fun clear(context: Context) {
        prefs(context).edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "renvest_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_BUSINESS_NAME = "business_name"
        private const val KEY_EMAIL = "email"
    }
}
