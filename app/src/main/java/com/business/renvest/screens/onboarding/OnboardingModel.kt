package com.business.renvest.screens.onboarding

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore

class OnboardingModel(
    private val authStore: AuthStore,
) {

    fun isStepDone(context: Context, key: String): Boolean =
        authStore.isOnboardingStepDone(context, key)

    fun markStep(context: Context, key: String) {
        authStore.markOnboardingStep(context, key)
    }

    fun completeOnboarding(context: Context) {
        authStore.setOnboardingComplete(context, true)
    }

    fun buildSteps(context: Context): List<OnboardingStepUi> = listOf(
        OnboardingStepUi(
            key = AuthStore.STEP_CUSTOMER,
            title = context.getString(R.string.onboarding_step_customer_title),
            description = context.getString(R.string.onboarding_step_customer_body),
            done = isStepDone(context, AuthStore.STEP_CUSTOMER),
            target = OnboardingTarget.Customers,
        ),
        OnboardingStepUi(
            key = AuthStore.STEP_PROMOTION,
            title = context.getString(R.string.onboarding_step_promotion_title),
            description = context.getString(R.string.onboarding_step_promotion_body),
            done = isStepDone(context, AuthStore.STEP_PROMOTION),
            target = OnboardingTarget.Promotions,
        ),
        OnboardingStepUi(
            key = AuthStore.STEP_ACTIVITY,
            title = context.getString(R.string.onboarding_step_activity_title),
            description = context.getString(R.string.onboarding_step_activity_body),
            done = isStepDone(context, AuthStore.STEP_ACTIVITY),
            target = OnboardingTarget.Activity,
        ),
        OnboardingStepUi(
            key = AuthStore.STEP_LOYALTY,
            title = context.getString(R.string.onboarding_step_loyalty_title),
            description = context.getString(R.string.onboarding_step_loyalty_body),
            done = isStepDone(context, AuthStore.STEP_LOYALTY),
            target = OnboardingTarget.Loyalty,
        ),
    )
}
