package com.business.renvest.data.repository

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.local.SessionLocalDataSource

class AuthRepositoryImpl(
    private val sessionLocalDataSource: SessionLocalDataSource,
) : AuthRepository {

    override fun isLoggedIn(context: Context): Boolean =
        sessionLocalDataSource.isLoggedIn(context)

    override fun getBusinessName(context: Context): String =
        sessionLocalDataSource.getBusinessName(context)

    override fun getEmail(context: Context): String =
        sessionLocalDataSource.getEmail(context)

    override fun signInWithEmail(context: Context, email: String): RenvestResult<Unit> =
        try {
            sessionLocalDataSource.signInWithEmail(context, email)
            RenvestResult.Ok(Unit)
        } catch (e: Exception) {
            RenvestResult.Err.Storage(e.message ?: "sign_in_failed")
        }

    override fun signUp(context: Context, businessName: String, email: String): RenvestResult<Unit> =
        try {
            sessionLocalDataSource.signUp(context, businessName, email)
            RenvestResult.Ok(Unit)
        } catch (e: Exception) {
            RenvestResult.Err.Storage(e.message ?: "sign_up_failed")
        }

    override fun clearSession(context: Context): RenvestResult<Unit> =
        try {
            sessionLocalDataSource.clear(context)
            RenvestResult.Ok(Unit)
        } catch (e: Exception) {
            RenvestResult.Err.Storage(e.message ?: "clear_session_failed")
        }
}
