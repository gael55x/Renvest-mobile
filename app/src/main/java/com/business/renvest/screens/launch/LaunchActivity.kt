package com.business.renvest.screens.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.screens.auth.LoginActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.startActivity

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authRepository().isLoggedIn(this)) {
            startActivity(DashboardActivity::class.java)
        } else {
            startActivity(LoginActivity::class.java)
        }
        finish()
    }
}
