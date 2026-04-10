package com.business.renvest.screens.launch

import android.content.Context
import com.business.renvest.data.repository.AuthRepository

class LaunchPresenter(
    private val view: LaunchContract.View,
    private val authRepository: AuthRepository,
    private val context: Context,
) : LaunchContract.Presenter {

    override fun start() {
        if (authRepository.isLoggedIn(context)) {
            view.navigateToDashboard()
        } else {
            view.navigateToLogin()
        }
        view.close()
    }
}
