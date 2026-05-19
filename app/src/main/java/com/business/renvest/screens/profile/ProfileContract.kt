package com.business.renvest.screens.profile

import android.content.Context
import java.io.File

interface ProfileContract {
    interface View {
        fun bindProfile(businessName: String, initials: String, emailDisplay: String, ownerLine: String)
        fun bindProfileLiveStats(members: String, returnOrPlaceholder: String, activePromotions: String)
        fun showLocalDataDisclaimer()
        fun showLogoutDialog()
        fun showEditBusinessDialog(
            businessName: String,
            businessType: String,
            location: String,
            onSubmit: (String, String, String) -> Unit,
        )
        fun shareExportFile(file: File)
        fun showComingSoon()
        fun showToast(message: String)
        fun navigateToLoginClearTask()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onLogoutClicked()
        fun onLogoutConfirmed(context: Context, clearLocalData: Boolean)
        fun onExportClicked(context: Context)
        fun onEditBusinessClicked(context: Context)
        fun onEditBusinessSubmitted(context: Context, name: String, type: String, location: String)
        fun onSettingsStubClicked()
    }
}
