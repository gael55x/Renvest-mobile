package com.business.renvest.screens.onboarding

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingPresenter(
    private val view: OnboardingContract.View,
    private val model: OnboardingModel,
    private val scope: CoroutineScope,
) : OnboardingContract.Presenter {

    override fun onViewReady(context: Context) {
        refresh(context)
    }

    override fun onStepClicked(context: Context, step: OnboardingStepUi) {
        model.markStep(context, step.key)
        when (step.target) {
            OnboardingTarget.Customers -> view.navigateToCustomers()
            OnboardingTarget.Promotions -> view.navigateToPromotions()
            OnboardingTarget.Activity -> view.navigateToActivity()
            OnboardingTarget.Loyalty -> view.navigateToLoyalty()
        }
        refresh(context)
    }

    override fun onFinishClicked(context: Context) {
        model.completeOnboarding(context)
        view.navigateToDashboard()
    }

    private fun refresh(context: Context) {
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            withContext(Dispatchers.Main) {
                view.bindSteps(model.buildSteps(context, counts))
            }
        }
    }
}
