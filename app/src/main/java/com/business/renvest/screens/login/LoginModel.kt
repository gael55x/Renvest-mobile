package com.business.renvest.screens.login

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.repository.AuthStore

class LoginModel(private val authStore: AuthStore) {

    fun signInWithEmail(context: Context, email: String, password: String): RenvestResult<Unit> =
        authStore.signInWithEmail(context, email, password)
}
