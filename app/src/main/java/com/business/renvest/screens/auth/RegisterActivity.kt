package com.business.renvest.screens.auth

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.data.notifyErrorIfNotOk
import com.business.renvest.data.RenvestResult
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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_register, R.id.root)

        val finishBack: () -> Unit = { finish() }
        findViewById<ImageButton>(R.id.button_back).setOnClickListener { finishBack() }
        findViewById<TextView>(R.id.text_back_nav).setOnClickListener { finishBack() }

        val textInputLayoutBusiness = findViewById<TextInputLayout>(R.id.input_business_layout)
        val textInputLayoutOwner = findViewById<TextInputLayout>(R.id.input_owner_layout)
        val textInputLayoutEmail = findViewById<TextInputLayout>(R.id.input_email_layout)
        val textInputLayoutPassword = findViewById<TextInputLayout>(R.id.input_password_layout)
        val textInputLayoutConfirm = findViewById<TextInputLayout>(R.id.input_confirm_password_layout)
        val materialButtonRegister = findViewById<MaterialButton>(R.id.button_register)
        val materialButtonGoLogin = findViewById<MaterialButton>(R.id.button_go_login)

        materialButtonGoLogin.setOnClickListener {
            startActivity(LoginActivity::class.java)
            finish()
        }

        materialButtonRegister.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okBusiness = textInputLayoutBusiness.validateRequired(requiredMessage)
            val okOwner = textInputLayoutOwner.validateRequired(requiredMessage)
            val okEmail = textInputLayoutEmail.validateRequired(requiredMessage)
            val okPassword = textInputLayoutPassword.validateRequired(requiredMessage, trim = false)
            val okConfirm = textInputLayoutConfirm.validateRequired(requiredMessage, trim = false)
            if (!okBusiness || !okOwner || !okEmail || !okPassword || !okConfirm) return@setOnClickListener

            val password = textInputLayoutPassword.valueText(trim = false)
            val confirm = textInputLayoutConfirm.valueText(trim = false)
            if (password != confirm) {
                textInputLayoutConfirm.error = getString(R.string.error_password_mismatch)
                return@setOnClickListener
            }
            textInputLayoutConfirm.error = null

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
                else -> signUpResult.notifyErrorIfNotOk { toast(it) }
            }
        }
    }
}
