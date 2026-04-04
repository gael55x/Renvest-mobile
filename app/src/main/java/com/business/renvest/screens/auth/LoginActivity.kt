package com.business.renvest.screens.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.data.RenvestResult
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.utils.applyEdgeToEdgeInsets
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.business.renvest.utils.validateRequired
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        applyEdgeToEdgeInsets(R.id.root)

        val textInputLayoutEmail = findViewById<TextInputLayout>(R.id.input_email_layout)
        val textInputLayoutPassword = findViewById<TextInputLayout>(R.id.input_password_layout)
        val materialButtonLogin = findViewById<MaterialButton>(R.id.button_login)
        val materialButtonGoRegister = findViewById<MaterialButton>(R.id.button_go_register)

        materialButtonLogin.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okEmail = textInputLayoutEmail.validateRequired(requiredMessage)
            val okPassword = textInputLayoutPassword.validateRequired(requiredMessage, trim = false)
            if (!okEmail || !okPassword) return@setOnClickListener
            when (
                val signInResult =
                    authRepository().signInWithEmail(this, textInputLayoutEmail.valueText())
            ) {
                is RenvestResult.Ok -> {
                    startActivityClearTask(DashboardActivity::class.java)
                    finish()
                }
                is RenvestResult.Err.Storage -> toast(signInResult.reason)
                is RenvestResult.Err.Network -> toast(signInResult.reason)
                is RenvestResult.Err.Validation -> toast(signInResult.reason)
            }
        }

        materialButtonGoRegister.setOnClickListener {
            startActivity(RegisterActivity::class.java)
        }
    }
}
