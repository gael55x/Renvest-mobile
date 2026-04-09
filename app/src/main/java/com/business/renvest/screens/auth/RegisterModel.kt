package com.business.renvest.screens.auth

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.repository.AuthRepository

class RegisterModel(private val authRepository: AuthRepository) {
    fun signUp(context: Context, businessName: String, email: String): RenvestResult<Unit> =
        authRepository.signUp(context, businessName, email)
}
