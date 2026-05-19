package com.business.renvest.screens.onboarding

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore

class OnboardingModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun isStepDone(context: Context, key: String): Boolean =
        authStore.isOnboardingStepDone(context, key)

    fun markStep(context: Context, key: String) {
        authStore.markOnboardingStep(context, key)
    }

    fun completeOnboarding(context: Context) {
        authStore.setOnboardingComplete(context, true)
    }

    fun buildSteps(context: Context, counts: LocalDataCounts): List<OnboardingStepUi> = listOf(
        OnboardingStepUi(
            key = AuthStore.STEP_CUSTOMER,
            title = context.getString(R.string.onboarding_step_customer_title),
            description = context.getString(R.string.onboarding_step_customer_body),
            done = isStepDone(context, AuthStore.STEP_CUSTOMER) || counts.customers > 0,
            target = OnboardingTarget.Customers,
        ),
        OnboardingStepUi(
            key = AuthStore.STEP_PROMOTION,
            title = context.getString(R.string.onboarding_step_promotion_title),
            description = context.getString(R.string.onboarding_step_promotion_body),
            done = isStepDone(context, AuthStore.STEP_PROMOTION) || counts.promotions > 0,
            target = OnboardingTarget.Promotions,
        ),
        OnboardingStepUi(
            key = AuthStore.STEP_ACTIVITY,
            title = context.getString(R.string.onboarding_step_activity_title),
            description = context.getString(R.string.onboarding_step_activity_body),
            done = isStepDone(context, AuthStore.STEP_ACTIVITY) || counts.activityEvents > 0,
            target = OnboardingTarget.Activity,
        ),
        OnboardingStepUi(
            key = AuthStore.STEP_LOYALTY,
            title = context.getString(R.string.onboarding_step_loyalty_title),
            description = context.getString(R.string.onboarding_step_loyalty_body),
            done = isStepDone(context, AuthStore.STEP_LOYALTY) || counts.loyaltyPrograms > 0,
            target = OnboardingTarget.Loyalty,
        ),
    )
}
