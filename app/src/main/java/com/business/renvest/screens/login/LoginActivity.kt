package com.business.renvest.screens.login

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.register.RegisterActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.business.renvest.utils.validateRequired
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_login, R.id.root)

        presenter = LoginPresenter(this, authStore())

        findViewById<TextView>(R.id.textviewForgotPassword).setOnClickListener {
            toast(getString(R.string.coming_soon))
        }

        val textinputEmailLayout = findViewById<TextInputLayout>(R.id.textinputEmailLayout)
        val textinputPasswordLayout = findViewById<TextInputLayout>(R.id.textinputPasswordLayout)
        val materialbuttonLogin = findViewById<MaterialButton>(R.id.buttonLogin)
        val materialbuttonGoRegister = findViewById<MaterialButton>(R.id.buttonGoRegister)

        materialbuttonLogin.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okEmail = textinputEmailLayout.validateRequired(requiredMessage)
            val okPassword = textinputPasswordLayout.validateRequired(requiredMessage, trim = false)
            if (!okEmail || !okPassword) return@setOnClickListener
            presenter.onLoginSubmitted(this, textinputEmailLayout.valueText())
        }

        materialbuttonGoRegister.setOnClickListener {
            startActivity(RegisterActivity::class.java)
        }
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateToDashboard() {
        startActivityClearTask(DashboardActivity::class.java)
        finish()
    }
}
