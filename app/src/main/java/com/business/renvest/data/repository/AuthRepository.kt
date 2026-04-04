package com.business.renvest.data.repository

import android.content.Context
import com.business.renvest.data.RenvestResult

interface AuthRepository {
    fun isLoggedIn(context: Context): Boolean
    fun getBusinessName(context: Context): String
    fun getEmail(context: Context): String
    fun signInWithEmail(context: Context, email: String): RenvestResult<Unit>
    fun signUp(context: Context, businessName: String, email: String): RenvestResult<Unit>
    fun clearSession(context: Context): RenvestResult<Unit>
}
