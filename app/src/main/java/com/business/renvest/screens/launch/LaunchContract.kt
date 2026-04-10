package com.business.renvest.screens.launch

interface LaunchContract {
    interface View {
        fun navigateToDashboard()
        fun navigateToLogin()
        fun close()
    }

    interface Presenter {
        fun start()
    }
}
