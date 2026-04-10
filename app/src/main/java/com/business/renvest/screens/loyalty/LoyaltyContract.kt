package com.business.renvest.screens.loyalty

import androidx.annotation.StringRes

interface LoyaltyContract {
    interface View {
        fun setStubTitle(@StringRes titleResId: Int)
    }

    interface Presenter {
        fun onViewReady()
    }
}
