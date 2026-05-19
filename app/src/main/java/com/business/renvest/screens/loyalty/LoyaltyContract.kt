package com.business.renvest.screens.loyalty

import android.content.Context
import androidx.annotation.StringRes

interface LoyaltyContract {
    interface View {
        fun setScreenTitle(@StringRes titleResId: Int)
        fun bindProgramsList(items: List<LoyaltyProgramRow>)
        fun setProgramsEmptyVisible(visible: Boolean)
        fun bindRemindersList(items: ArrayList<LoyaltyReminderRow>)
        fun refreshRemindersList()
        fun showMessage(message: String)
        fun showAddProgramDialog(onSubmit: (String, Int, String) -> Unit)
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onAddProgramClicked(context: Context)
        fun onAddProgramSubmitted(context: Context, name: String, visits: Int, reward: String)
        fun onProgramLongClicked(context: Context, position: Int): Boolean
        fun onReminderClicked(context: Context, position: Int)
        fun onReminderLongClicked(context: Context, position: Int): Boolean
        fun onAddReminderClicked(context: Context, rawTitle: String)
    }
}
