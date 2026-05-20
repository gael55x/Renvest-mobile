package com.business.renvest.screens.login

import android.content.Context

interface LoginContract {
    interface View {
        fun showToast(message: String)
        fun setLoginInProgress(inProgress: Boolean)
        fun navigateToDashboard()
        fun navigateToOnboarding()
    }

    interface Presenter {
        fun onLoginSubmitted(context: Context, email: String, password: String)
    }
}
