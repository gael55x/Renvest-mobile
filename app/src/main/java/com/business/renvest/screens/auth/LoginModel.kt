package com.business.renvest.screens.auth

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.repository.AuthRepository

class LoginModel(private val authRepository: AuthRepository) {
    fun signInWithEmail(context: Context, email: String): RenvestResult<Unit> =
        authRepository.signInWithEmail(context, email)
}
