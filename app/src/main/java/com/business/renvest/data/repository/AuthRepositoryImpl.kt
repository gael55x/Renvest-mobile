package com.business.renvest.data.repository

import android.content.Context
import com.business.renvest.data.RenvestResult

class AuthRepositoryImpl : AuthRepository {

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun isLoggedIn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGGED_IN, false)

    override fun getBusinessName(context: Context): String =
        prefs(context).getString(KEY_BUSINESS_NAME, "").orEmpty()

    override fun getEmail(context: Context): String =
        prefs(context).getString(KEY_EMAIL, "").orEmpty()

    override fun signInWithEmail(context: Context, email: String): RenvestResult<Unit> =
        try {
            prefs(context).edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_EMAIL, email.trim())
                .apply()
            RenvestResult.Ok(Unit)
        } catch (e: Exception) {
            RenvestResult.Err.Storage(e.message ?: "sign_in_failed")
        }

    override fun signUp(context: Context, businessName: String, email: String): RenvestResult<Unit> =
        try {
            prefs(context).edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_BUSINESS_NAME, businessName.trim())
                .putString(KEY_EMAIL, email.trim())
                .apply()
            RenvestResult.Ok(Unit)
        } catch (e: Exception) {
            RenvestResult.Err.Storage(e.message ?: "sign_up_failed")
        }

    override fun clearSession(context: Context): RenvestResult<Unit> =
        try {
            prefs(context).edit().clear().apply()
            RenvestResult.Ok(Unit)
        } catch (e: Exception) {
            RenvestResult.Err.Storage(e.message ?: "clear_session_failed")
        }

    companion object {
        private const val PREFS_NAME = "renvest_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_BUSINESS_NAME = "business_name"
        private const val KEY_EMAIL = "email"
    }
}
