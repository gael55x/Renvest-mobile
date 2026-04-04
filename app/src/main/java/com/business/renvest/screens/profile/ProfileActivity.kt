package com.business.renvest.screens.profile

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.data.RenvestResult
import com.business.renvest.screens.auth.LoginActivity
import com.business.renvest.utils.applyEdgeToEdgeInsets
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        applyEdgeToEdgeInsets(R.id.root)

        val stringBusinessStored = authRepository().getBusinessName(this).trim()
        val stringEmailStored = authRepository().getEmail(this).trim()

        val textViewProfileBusiness = findViewById<TextView>(R.id.text_profile_business)
        val textViewProfileEmail = findViewById<TextView>(R.id.text_profile_email)
        val materialButtonLogout = findViewById<MaterialButton>(R.id.button_logout)

        textViewProfileBusiness.text =
            if (stringBusinessStored.isNotEmpty()) {
                stringBusinessStored
            } else {
                getString(R.string.profile_not_set)
            }

        textViewProfileEmail.text =
            if (stringEmailStored.isNotEmpty()) {
                stringEmailStored
            } else {
                getString(R.string.profile_not_set)
            }

        materialButtonLogout.setOnClickListener {
            when (val clearResult = authRepository().clearSession(this)) {
                is RenvestResult.Ok -> {
                    startActivityClearTask(LoginActivity::class.java)
                    finish()
                }
                is RenvestResult.Err.Storage -> toast(clearResult.reason)
                is RenvestResult.Err.Network -> toast(clearResult.reason)
                is RenvestResult.Err.Validation -> toast(clearResult.reason)
            }
        }
    }
}
