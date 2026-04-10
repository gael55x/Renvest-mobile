package com.business.renvest.screens.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.login.LoginActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_profile, R.id.root)

        presenter = ProfilePresenter(this, ProfileModel(authStore()))
        presenter.onViewReady(this)

        setupMainBottomNavigation(R.id.nav_profile)

        val stub = View.OnClickListener { presenter.onSettingsStubClicked() }
        findViewById<View>(R.id.button_profile_overflow).setOnClickListener(stub)
        findViewById<View>(R.id.button_profile_hero_edit).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_business_name).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_business_type).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_email).setOnClickListener(stub)
        findViewById<View>(R.id.row_settings_location).setOnClickListener(stub)
        findViewById<View>(R.id.row_loyalty_threshold).setOnClickListener(stub)
        findViewById<View>(R.id.row_loyalty_points_mode).setOnClickListener(stub)

        findViewById<MaterialButton>(R.id.button_logout).setOnClickListener {
            presenter.onLogoutClicked(this)
        }
    }

    override fun bindProfile(businessName: String, initials: String, emailDisplay: String) {
        findViewById<TextView>(R.id.text_header_business).text = businessName
        findViewById<TextView>(R.id.text_profile_hero_business_name).text = businessName
        findViewById<TextView>(R.id.text_profile_hero_initials).text = initials
        findViewById<TextView>(R.id.text_profile_row_business_value).text = businessName
        findViewById<TextView>(R.id.text_profile_row_email_value).text = emailDisplay
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateToLoginClearTask() {
        startActivityClearTask(LoginActivity::class.java)
        finish()
    }
}
