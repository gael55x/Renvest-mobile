package com.business.renvest.screens.auth

import android.content.Context

interface RegisterContract {
    interface View {
        fun showToast(message: String)
        fun navigateToDashboard()
        fun setConfirmPasswordError(message: String)
        fun clearConfirmPasswordError()
    }

    interface Presenter {
        fun onRegisterSubmitted(
            context: Context,
            businessName: String,
            email: String,
            password: String,
            confirmPassword: String,
            passwordMismatchMessage: String,
        )
    }
}
