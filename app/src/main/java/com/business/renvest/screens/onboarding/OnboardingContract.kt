package com.business.renvest.screens.onboarding

import android.content.Context

interface OnboardingContract {
    interface View {
        fun bindProgress(completed: Int, total: Int)
        fun bindSteps(steps: List<OnboardingStepUi>)
        fun navigateToDashboard()
        fun navigateToCustomers()
        fun navigateToPromotions()
        fun navigateToActivity()
        fun navigateToLoyalty()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onStepClicked(context: Context, step: OnboardingStepUi)
        fun onFinishClicked(context: Context)
    }
}

data class OnboardingStepUi(
    val key: String,
    val title: String,
    val description: String,
    val done: Boolean,
    val target: OnboardingTarget,
)

enum class OnboardingTarget {
    Customers,
    Promotions,
    Activity,
    Loyalty,
}
