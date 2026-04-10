package com.business.renvest.screens.login

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk
import com.business.renvest.data.repository.AuthRepository

class LoginPresenter(
    private val view: LoginContract.View,
    private val authRepository: AuthRepository,
) : LoginContract.Presenter {

    override fun onLoginSubmitted(context: Context, email: String) {
        when (val result = authRepository.signInWithEmail(context, email)) {
            is RenvestResult.Ok -> view.navigateToDashboard()
            else -> result.notifyErrorIfNotOk { view.showToast(it) }
        }
    }
}
