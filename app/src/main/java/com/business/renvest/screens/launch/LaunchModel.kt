package com.business.renvest.screens.launch

import android.content.Context
import com.business.renvest.data.repository.AuthRepository

class LaunchModel(private val authRepository: AuthRepository) {
    fun isLoggedIn(context: Context): Boolean = authRepository.isLoggedIn(context)
}
