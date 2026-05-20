package com.business.renvest.screens.loyalty

import android.content.Context
import androidx.annotation.IdRes

interface LoyaltyContract {
    interface View {
        fun setupBottomNav(@IdRes selectedItemId: Int, activityBadgeCount: Int)
        fun bindHeader(businessName: String)
        fun bindProgramsList(items: List<LoyaltyProgramRow>)
        fun setProgramsEmptyVisible(visible: Boolean)
        fun bindRemindersList(items: List<LoyaltyReminderRow>)
        fun setRemindersEmptyVisible(visible: Boolean)
        fun showMessage(message: String)
        fun showAddProgramDialog(onSubmit: (String, Int, String) -> Unit)
        fun showAddReminderDialog(onSubmit: (String) -> Unit)
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onAddProgramClicked(context: Context)
        fun onAddProgramSubmitted(context: Context, name: String, visits: Int, reward: String)
        fun onProgramLongClicked(context: Context, position: Int): Boolean
        fun onReminderClicked(context: Context, position: Int)
        fun onReminderLongClicked(context: Context, position: Int): Boolean
        fun onAddReminderClicked(context: Context)
        fun onAddReminderSubmitted(context: Context, rawTitle: String)
    }
}
