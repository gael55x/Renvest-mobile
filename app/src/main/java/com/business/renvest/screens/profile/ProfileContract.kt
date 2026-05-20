package com.business.renvest.screens.profile

import android.content.Context
import androidx.annotation.IdRes
import java.io.File

interface ProfileContract {
    interface View {
        fun setupBottomNav(@IdRes selectedItemId: Int, activityBadgeCount: Int)
        fun bindProfile(businessName: String, initials: String, emailDisplay: String, ownerLine: String)
        fun bindProfileLiveStats(members: String, returnOrPlaceholder: String, activePromotions: String)
        fun bindLoyaltySettings(thresholdLabel: String, pointsModeLabel: String)
        fun showLoyaltyThresholdDialog(currentPoints: Int, onSubmit: (String) -> Unit)
        fun showLoyaltyPointsModeDialog(checkedIndex: Int, options: Array<String>, onSelected: (Int) -> Unit)
        fun showLocalDataDisclaimer()
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
        fun onLogoutClicked(context: Context)
        fun onExportClicked(context: Context)
        fun onEditBusinessClicked(context: Context)
        fun onEditBusinessSubmitted(context: Context, name: String, type: String, location: String)
        fun onLoyaltyThresholdClicked(context: Context)
        fun onLoyaltyThresholdSubmitted(context: Context, rawPoints: String)
        fun onLoyaltyPointsModeClicked(context: Context)
        fun onLoyaltyPointsModeSelected(context: Context, mode: String)
        fun onSettingsStubClicked()
    }
}
