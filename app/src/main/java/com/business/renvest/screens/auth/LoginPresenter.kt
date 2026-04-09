package com.business.renvest.screens.auth

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginModel,
) : LoginContract.Presenter {

    override fun onLoginSubmitted(context: Context, email: String) {
        when (val result = model.signInWithEmail(context, email)) {
            is RenvestResult.Ok -> view.navigateToDashboard()
            else -> result.notifyErrorIfNotOk { view.showToast(it) }
        }
    }
}
