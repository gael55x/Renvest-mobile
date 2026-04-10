package com.business.renvest.screens.launch

import android.content.Context
import com.business.renvest.data.repository.AuthStore

class LaunchPresenter(
    private val view: LaunchContract.View,
    private val authStore: AuthStore,
    private val context: Context,
) : LaunchContract.Presenter {

    override fun start() {
        if (authStore.isLoggedIn(context)) {
            view.navigateToDashboard()
        } else {
            view.navigateToLogin()
        }
        view.close()
    }
}
