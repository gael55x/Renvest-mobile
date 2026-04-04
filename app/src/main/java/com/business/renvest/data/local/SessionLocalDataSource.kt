package com.business.renvest.data.local

import android.content.Context

interface SessionLocalDataSource {
    fun isLoggedIn(context: Context): Boolean
    fun getBusinessName(context: Context): String
    fun getEmail(context: Context): String
    fun signInWithEmail(context: Context, email: String)
    fun signUp(context: Context, businessName: String, email: String)
    fun clear(context: Context)
}
