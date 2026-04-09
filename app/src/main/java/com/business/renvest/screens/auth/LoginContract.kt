package com.business.renvest.screens.auth

import android.content.Context

interface LoginContract {
    interface View {
        fun showToast(message: String)
        fun navigateToDashboard()
    }

    interface Presenter {
        fun onLoginSubmitted(context: Context, email: String)
    }
}
