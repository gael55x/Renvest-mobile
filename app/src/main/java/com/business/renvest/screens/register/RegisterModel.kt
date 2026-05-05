package com.business.renvest.screens.register

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.repository.AuthStore

class RegisterModel(private val authStore: AuthStore) {

    fun signUp(context: Context, businessName: String, email: String): RenvestResult<Unit> =
        authStore.signUp(context, businessName, email)
}
