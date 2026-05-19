package com.business.renvest.screens.register

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.onboarding.OnboardingActivity
import com.business.renvest.screens.login.LoginActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.business.renvest.utils.validateRequired
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var presenter: RegisterPresenter
    private lateinit var textinputConfirmPasswordLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_register, R.id.root)

        presenter = RegisterPresenter(this, RegisterModel(authStore()))

        val finishBack: () -> Unit = { finish() }
        val imagebuttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val textviewBackNav = findViewById<TextView>(R.id.textviewBackNav)
        imagebuttonBack.setOnClickListener { finishBack() }
        textviewBackNav.setOnClickListener { finishBack() }

        val textinputBusinessLayout = findViewById<TextInputLayout>(R.id.textinputBusinessLayout)
        val textinputOwnerLayout = findViewById<TextInputLayout>(R.id.textinputOwnerLayout)
        val textinputEmailLayout = findViewById<TextInputLayout>(R.id.textinputEmailLayout)
        val textinputPasswordLayout = findViewById<TextInputLayout>(R.id.textinputPasswordLayout)
        textinputConfirmPasswordLayout = findViewById(R.id.textinputConfirmPasswordLayout)
        val materialbuttonRegister = findViewById<MaterialButton>(R.id.buttonRegister)
        val materialbuttonGoLogin = findViewById<MaterialButton>(R.id.buttonGoLogin)

        materialbuttonGoLogin.setOnClickListener {
            startActivity(LoginActivity::class.java)
            finish()
        }

        materialbuttonRegister.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okBusiness = textinputBusinessLayout.validateRequired(requiredMessage)
            val okOwner = textinputOwnerLayout.validateRequired(requiredMessage)
            val okEmail = textinputEmailLayout.validateRequired(requiredMessage)
            val okPassword = textinputPasswordLayout.validateRequired(requiredMessage, trim = false)
            val okConfirm = textinputConfirmPasswordLayout.validateRequired(requiredMessage, trim = false)
            if (!okBusiness || !okOwner || !okEmail || !okPassword || !okConfirm) return@setOnClickListener

            presenter.onRegisterSubmitted(
                this,
                textinputBusinessLayout.valueText(),
                textinputOwnerLayout.valueText(),
                textinputEmailLayout.valueText(),
                textinputPasswordLayout.valueText(trim = false),
                textinputConfirmPasswordLayout.valueText(trim = false),
                getString(R.string.error_password_mismatch),
            )
        }
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateToOnboarding() {
        startActivityClearTask(OnboardingActivity::class.java)
        finish()
    }

    override fun setConfirmPasswordError(message: String) {
        textinputConfirmPasswordLayout.error = message
    }

    override fun clearConfirmPasswordError() {
        textinputConfirmPasswordLayout.error = null
    }
}
