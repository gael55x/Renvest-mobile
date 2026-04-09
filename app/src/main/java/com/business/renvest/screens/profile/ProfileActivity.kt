package com.business.renvest.screens.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.data.RenvestResult
import com.business.renvest.screens.auth.LoginActivity
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.displayBusinessName
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_profile, R.id.root)

        val stringEmailStored = authRepository().getEmail(this).trim()
        val displayBusiness = displayBusinessName()

        findViewById<TextView>(R.id.text_header_business).text = displayBusiness
        findViewById<TextView>(R.id.text_profile_hero_business_name).text = displayBusiness
        findViewById<TextView>(R.id.text_profile_hero_initials).text = initialsFromName(displayBusiness)
        findViewById<TextView>(R.id.text_profile_row_business_value).text = displayBusiness
        findViewById<TextView>(R.id.text_profile_row_email_value).text =
            if (stringEmailStored.isNotEmpty()) {
                stringEmailStored
            } else {
                getString(R.string.profile_email_placeholder)
            }

        setupMainBottomNavigation(R.id.nav_profile)

        val coming = getString(R.string.coming_soon)
        val stub = View.OnClickListener { toast(coming) }
        findViewById<View>(R.id.button_profile_overflow).setOnClickListener(stub)
        findViewById<View>(R.id.button_profile_hero_edit).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_business_name).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_business_type).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_email).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_location).setOnClickListener(stub)
        findViewById<View>(R.id.row_loyalty_threshold).setOnClickListener(stub)
        findViewById<View>(R.id.row_loyalty_points_mode).setOnClickListener(stub)

        findViewById<MaterialButton>(R.id.button_logout).setOnClickListener {
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

    private fun initialsFromName(name: String): String {
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        return when {
            parts.isEmpty() -> "RV"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts.last().first()}".uppercase()
        }
    }
}
