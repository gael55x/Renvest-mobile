package com.business.renvest.screens.launch

import android.content.Context

class LaunchPresenter(
    private val view: LaunchContract.View,
    private val model: LaunchModel,
    private val context: Context,
) : LaunchContract.Presenter {

    override fun start() {
        if (model.isLoggedIn(context)) {
            view.navigateToDashboard()
        } else {
            view.navigateToLogin()
        }
        view.close()
    }
}
