package com.business.renvest.screens.promotions

import android.content.Context
import androidx.annotation.IdRes

interface PromotionsContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int)
        fun showComingSoon()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onStubInteraction()
    }
}
