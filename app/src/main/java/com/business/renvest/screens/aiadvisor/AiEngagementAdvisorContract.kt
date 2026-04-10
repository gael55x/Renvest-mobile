package com.business.renvest.screens.aiadvisor

import android.content.Context
import androidx.annotation.IdRes

interface AiEngagementAdvisorContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int)
        fun setEngagementProgress(percent: Int)
        fun showComingSoon()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onStubInteraction()
    }
}
