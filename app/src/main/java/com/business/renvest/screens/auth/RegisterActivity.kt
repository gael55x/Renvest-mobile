package com.business.renvest.screens.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.data.RenvestResult
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.utils.applyEdgeToEdgeInsets
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.business.renvest.utils.validateRequired
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        applyEdgeToEdgeInsets(R.id.root)

        val textInputLayoutBusiness = findViewById<TextInputLayout>(R.id.input_business_layout)
        val textInputLayoutEmail = findViewById<TextInputLayout>(R.id.input_email_layout)
        val textInputLayoutPassword = findViewById<TextInputLayout>(R.id.input_password_layout)
        val materialButtonRegister = findViewById<MaterialButton>(R.id.button_register)

        materialButtonRegister.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okBusiness = textInputLayoutBusiness.validateRequired(requiredMessage)
            val okEmail = textInputLayoutEmail.validateRequired(requiredMessage)
            val okPassword = textInputLayoutPassword.validateRequired(requiredMessage, trim = false)
            if (!okBusiness || !okEmail || !okPassword) return@setOnClickListener
            when (
                val signUpResult = authRepository().signUp(
                    this,
                    textInputLayoutBusiness.valueText(),
                    textInputLayoutEmail.valueText(),
                )
            ) {
                is RenvestResult.Ok -> {
                    startActivityClearTask(DashboardActivity::class.java)
                    finish()
                }
                is RenvestResult.Err.Storage -> toast(signUpResult.reason)
                is RenvestResult.Err.Network -> toast(signUpResult.reason)
                is RenvestResult.Err.Validation -> toast(signUpResult.reason)
            }
        }
    }
}
