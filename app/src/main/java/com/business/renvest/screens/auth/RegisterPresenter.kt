package com.business.renvest.screens.auth

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val model: RegisterModel,
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
        when (val result = model.signUp(context, businessName, email)) {
            is RenvestResult.Ok -> view.navigateToDashboard()
            else -> result.notifyErrorIfNotOk { view.showToast(it) }
        }
    }
}
