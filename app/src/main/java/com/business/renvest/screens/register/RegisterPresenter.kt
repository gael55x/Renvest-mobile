package com.business.renvest.screens.register

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk
import com.business.renvest.data.repository.AuthStore

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val authStore: AuthStore,
) : RegisterContract.Presenter {

    override fun onRegisterSubmitted(
        context: Context,
        businessName: String,
        email: String,
        password: String,
        confirmPassword: String,
        passwordMismatchMessage: String,
    ) {
        if (password != confirmPassword) {
            view.setConfirmPasswordError(passwordMismatchMessage)
            return
        }
        view.clearConfirmPasswordError()
        when (val result = authStore.signUp(context, businessName, email)) {
            is RenvestResult.Ok -> view.navigateToDashboard()
            else -> result.notifyErrorIfNotOk { view.showToast(it) }
        }
    }
}
