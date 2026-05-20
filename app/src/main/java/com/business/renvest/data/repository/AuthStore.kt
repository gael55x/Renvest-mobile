package com.business.renvest.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.business.renvest.R
import com.business.renvest.app.RenvestApp
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.security.LocalPasswordHasher

class AuthStore(
    private val sessionPreferences: SessionPreferences? = null,
) {

    private fun session(context: Context): SharedPreferences {
        val app = context.applicationContext
        val holder = sessionPreferences
            ?: (app as? RenvestApp)?.sessionPreferences
            ?: SessionPreferences(app)
        return holder.prefs
    }

    fun isLoggedIn(context: Context): Boolean =
        session(context).getBoolean(KEY_LOGGED_IN, false)

    fun getBusinessName(context: Context): String =
        session(context).getString(KEY_BUSINESS_NAME, "").orEmpty()

    fun getOwnerName(context: Context): String =
        session(context).getString(KEY_OWNER_NAME, "").orEmpty()

    fun getEmail(context: Context): String =
        session(context).getString(KEY_EMAIL, "").orEmpty()

    fun getBusinessType(context: Context): String =
        session(context).getString(KEY_BUSINESS_TYPE, "").orEmpty()

    fun getBusinessLocation(context: Context): String =
        session(context).getString(KEY_BUSINESS_LOCATION, "").orEmpty()

    fun signInWithEmail(context: Context, email: String, password: String): RenvestResult<Unit> {
        val prefs = session(context)
        val storedEmail = prefs.getString(KEY_EMAIL, "").orEmpty()
        val storedHash = prefs.getString(KEY_PASSWORD_HASH, "").orEmpty()
        val salt = prefs.getString(KEY_PASSWORD_SALT, "").orEmpty()
        if (storedHash.isEmpty()) {
            return RenvestResult.Err.Validation(context.getString(R.string.error_no_account))
        }
        if (!storedEmail.equals(email.trim(), ignoreCase = true)) {
            return RenvestResult.Err.Validation(context.getString(R.string.error_wrong_email))
        }
        if (!LocalPasswordHasher.verify(password, salt, storedHash)) {
            return RenvestResult.Err.Validation(context.getString(R.string.error_wrong_passcode))
        }
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email.trim())
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun signUp(
        context: Context,
        businessName: String,
        ownerName: String,
        email: String,
        password: String,
    ): RenvestResult<Unit> {
        val salt = LocalPasswordHasher.generateSalt()
        val hash = LocalPasswordHasher.hash(password, salt)
        session(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_BUSINESS_NAME, businessName.trim())
            .putString(KEY_OWNER_NAME, ownerName.trim())
            .putString(KEY_EMAIL, email.trim())
            .putString(KEY_PASSWORD_SALT, salt)
            .putString(KEY_PASSWORD_HASH, hash)
            .putBoolean(KEY_ONBOARDING_COMPLETE, false)
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun updateBusinessProfile(
        context: Context,
        businessName: String,
        businessType: String,
        location: String,
    ): RenvestResult<Unit> {
        session(context).edit()
            .putString(KEY_BUSINESS_NAME, businessName.trim())
            .putString(KEY_BUSINESS_TYPE, businessType.trim())
            .putString(KEY_BUSINESS_LOCATION, location.trim())
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun clearSession(context: Context): RenvestResult<Unit> {
        session(context).edit()
            .putBoolean(KEY_LOGGED_IN, false)
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun isOnboardingComplete(context: Context): Boolean =
        session(context).getBoolean(KEY_ONBOARDING_COMPLETE, false)

    fun setOnboardingComplete(context: Context, complete: Boolean) {
        session(context).edit()
            .putBoolean(KEY_ONBOARDING_COMPLETE, complete)
            .apply()
    }

    fun markOnboardingStep(context: Context, stepKey: String) {
        session(context).edit()
            .putBoolean(stepKey, true)
            .apply()
    }

    fun isOnboardingStepDone(context: Context, stepKey: String): Boolean =
        session(context).getBoolean(stepKey, false)

    fun businessDisplayName(context: Context): String {
        val stored = getBusinessName(context).trim()
        return if (stored.isNotEmpty()) stored else context.getString(R.string.default_business_display)
    }

    companion object {
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_BUSINESS_NAME = "business_name"
        private const val KEY_OWNER_NAME = "owner_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_BUSINESS_TYPE = "business_type"
        private const val KEY_BUSINESS_LOCATION = "business_location"
        private const val KEY_PASSWORD_SALT = "password_salt"
        private const val KEY_PASSWORD_HASH = "password_hash"
        const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
        const val STEP_CUSTOMER = "onboarding_step_customer"
        const val STEP_PROMOTION = "onboarding_step_promotion"
        const val STEP_ACTIVITY = "onboarding_step_activity"
        const val STEP_LOYALTY = "onboarding_step_loyalty"
    }
}
