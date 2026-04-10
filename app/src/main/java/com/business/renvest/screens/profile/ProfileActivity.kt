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

        setupMainBottomNavigation(R.id.navProfile)

        val stub = View.OnClickListener { presenter.onSettingsStubClicked() }
        findViewById<View>(R.id.buttonProfileOverflow).setOnClickListener(stub)
        findViewById<View>(R.id.buttonProfileHeroEdit).setOnClickListener(stub)
        findViewById<View>(R.id.rowSettingsBusinessName).setOnClickListener(stub)
        findViewById<View>(R.id.rowSettingsBusinessType).setOnClickListener(stub)
        findViewById<View>(R.id.rowSettingsEmail).setOnClickListener(stub)
        findViewById<View>(R.id.rowSettingsLocation).setOnClickListener(stub)
        findViewById<View>(R.id.rowLoyaltyThreshold).setOnClickListener(stub)
        findViewById<View>(R.id.rowLoyaltyPointsMode).setOnClickListener(stub)

        findViewById<MaterialButton>(R.id.buttonLogout).setOnClickListener {
            presenter.onLogoutClicked(this)
        }
    }

    override fun bindProfile(businessName: String, initials: String, emailDisplay: String) {
        findViewById<TextView>(R.id.textviewHeaderBusiness).text = businessName
        findViewById<TextView>(R.id.textviewProfileHeroBusinessName).text = businessName
        findViewById<TextView>(R.id.textviewProfileHeroInitials).text = initials
        findViewById<TextView>(R.id.textviewProfileRowBusinessValue).text = businessName
        findViewById<TextView>(R.id.textviewProfileRowEmailValue).text = emailDisplay
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
