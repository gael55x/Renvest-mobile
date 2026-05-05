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
    private lateinit var textviewHeaderBusiness: TextView
    private lateinit var textviewProfileHeroBusinessName: TextView
    private lateinit var textviewProfileHeroInitials: TextView
    private lateinit var textviewProfileRowBusinessValue: TextView
    private lateinit var textviewProfileRowEmailValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_profile, R.id.root)

        textviewHeaderBusiness = findViewById(R.id.textviewHeaderBusiness)
        textviewProfileHeroBusinessName = findViewById(R.id.textviewProfileHeroBusinessName)
        textviewProfileHeroInitials = findViewById(R.id.textviewProfileHeroInitials)
        textviewProfileRowBusinessValue = findViewById(R.id.textviewProfileRowBusinessValue)
        textviewProfileRowEmailValue = findViewById(R.id.textviewProfileRowEmailValue)

        presenter = ProfilePresenter(this, ProfileModel(authStore()))
        presenter.onViewReady(this)

        setupMainBottomNavigation(R.id.navProfile)

        val stub = View.OnClickListener { presenter.onSettingsStubClicked() }
        val buttonProfileOverflow = findViewById<View>(R.id.buttonProfileOverflow)
        val buttonProfileHeroEdit = findViewById<View>(R.id.buttonProfileHeroEdit)
        val rowSettingsBusinessName = findViewById<View>(R.id.rowSettingsBusinessName)
        val rowSettingsBusinessType = findViewById<View>(R.id.rowSettingsBusinessType)
        val rowSettingsEmail = findViewById<View>(R.id.rowSettingsEmail)
        val rowSettingsLocation = findViewById<View>(R.id.rowSettingsLocation)
        val rowLoyaltyThreshold = findViewById<View>(R.id.rowLoyaltyThreshold)
        val rowLoyaltyPointsMode = findViewById<View>(R.id.rowLoyaltyPointsMode)

        buttonProfileOverflow.setOnClickListener(stub)
        buttonProfileHeroEdit.setOnClickListener(stub)
        rowSettingsBusinessName.setOnClickListener(stub)
        rowSettingsBusinessType.setOnClickListener(stub)
        rowSettingsEmail.setOnClickListener(stub)
        rowSettingsLocation.setOnClickListener(stub)
        rowLoyaltyThreshold.setOnClickListener(stub)
        rowLoyaltyPointsMode.setOnClickListener(stub)

        val materialbuttonLogout = findViewById<MaterialButton>(R.id.buttonLogout)
        materialbuttonLogout.setOnClickListener {
            presenter.onLogoutClicked(this)
        }
    }

    override fun bindProfile(businessName: String, initials: String, emailDisplay: String) {
        textviewHeaderBusiness.text = businessName
        textviewProfileHeroBusinessName.text = businessName
        textviewProfileHeroInitials.text = initials
        textviewProfileRowBusinessValue.text = businessName
        textviewProfileRowEmailValue.text = emailDisplay
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
