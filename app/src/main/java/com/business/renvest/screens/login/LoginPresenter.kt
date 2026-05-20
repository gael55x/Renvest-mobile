package com.business.renvest.screens.login

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginModel,
    private val scope: CoroutineScope,
) : LoginContract.Presenter {

    override fun onLoginSubmitted(context: Context, email: String, password: String) {
        view.setLoginInProgress(true)
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                model.signInWithEmail(context, email, password)
            }
            withContext(Dispatchers.Main) {
                view.setLoginInProgress(false)
                when (result) {
                    is RenvestResult.Ok -> {
                        if (model.isOnboardingComplete(context)) {
                            view.navigateToDashboard()
                        } else {
                            view.navigateToOnboarding()
                        }
                    }
                    else -> result.notifyErrorIfNotOk { view.showToast(it) }
                }
            }
        }
    }
}
