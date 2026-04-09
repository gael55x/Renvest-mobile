package com.business.renvest.screens.auth

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.utils.authRepository
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

        presenter = LoginPresenter(this, LoginModel(authRepository()))

        findViewById<TextView>(R.id.text_forgot_password).setOnClickListener {
            toast(getString(R.string.coming_soon))
        }

        val textInputLayoutEmail = findViewById<TextInputLayout>(R.id.input_email_layout)
        val textInputLayoutPassword = findViewById<TextInputLayout>(R.id.input_password_layout)
        val materialButtonLogin = findViewById<MaterialButton>(R.id.button_login)
        val materialButtonGoRegister = findViewById<MaterialButton>(R.id.button_go_register)

        materialButtonLogin.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okEmail = textInputLayoutEmail.validateRequired(requiredMessage)
            val okPassword = textInputLayoutPassword.validateRequired(requiredMessage, trim = false)
            if (!okEmail || !okPassword) return@setOnClickListener
            presenter.onLoginSubmitted(this, textInputLayoutEmail.valueText())
        }

        materialButtonGoRegister.setOnClickListener {
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
