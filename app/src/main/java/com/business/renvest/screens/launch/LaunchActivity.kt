package com.business.renvest.screens.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.login.LoginActivity
import com.business.renvest.screens.onboarding.OnboardingActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask

class LaunchActivity : AppCompatActivity(), LaunchContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LaunchPresenter(this, LaunchModel(authStore()), this).start()
    }

    override fun navigateToDashboard() {
        startActivityClearTask(DashboardActivity::class.java)
    }

    override fun navigateToOnboarding() {
        startActivityClearTask(OnboardingActivity::class.java)
    }

    override fun navigateToLogin() {
        startActivity(LoginActivity::class.java)
    }

    override fun close() {
        finish()
    }
}
