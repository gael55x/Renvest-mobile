package com.business.renvest.screens.loyalty

import android.content.Context
import androidx.annotation.StringRes

interface LoyaltyContract {
    interface View {
        fun setStubTitle(@StringRes titleResId: Int)
        fun bindRemindersList(items: ArrayList<LoyaltyReminderRow>)
        fun refreshRemindersList()
        fun showMessage(message: String)
    }

    interface Presenter {
        fun onViewReady()
        fun onReminderClicked(context: Context, position: Int)
        fun onReminderLongClicked(context: Context, position: Int): Boolean
        fun onAddReminderClicked(context: Context, rawTitle: String)
    }
}
