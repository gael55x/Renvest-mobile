package com.business.renvest.screens.aiadvisor

import android.content.Context
import androidx.annotation.IdRes

interface AiEngagementAdvisorContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int)
        fun bindLocalAdvisor(title: String, body: String)
        fun navigateTo(target: Class<*>)
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onActivatePromoClicked(context: Context)
        fun onViewDataClicked(context: Context)
    }
}
