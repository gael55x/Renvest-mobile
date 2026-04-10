package com.business.renvest.screens.profile

import android.content.Context

interface ProfileContract {
    interface View {
        fun bindProfile(businessName: String, initials: String, emailDisplay: String)
        fun showComingSoon()
        fun showToast(message: String)
        fun navigateToLoginClearTask()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onLogoutClicked(context: Context)
        fun onSettingsStubClicked()
    }
}
