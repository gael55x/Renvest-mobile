package com.business.renvest.screens.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.login.LoginActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setClickListeners
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.business.renvest.utils.toastComingSoon
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_profile, R.id.root)

        presenter = ProfilePresenter(this, ProfileModel(authStore(), renvestDb()))
        presenter.onViewReady(this)

        setupMainBottomNavigation(R.id.navProfile)

        val stub = View.OnClickListener { presenter.onSettingsStubClicked() }
        setClickListeners(
            stub,
            R.id.buttonProfileOverflow,
            R.id.buttonProfileHeroEdit,
            R.id.rowSettingsBusinessName,
            R.id.rowSettingsBusinessType,
            R.id.rowSettingsEmail,
            R.id.rowSettingsLocation,
            R.id.rowLoyaltyThreshold,
            R.id.rowLoyaltyPointsMode,
        )

        val materialbuttonLogout = findViewById<MaterialButton>(R.id.buttonLogout)
        materialbuttonLogout.setOnClickListener {
            presenter.onLogoutClicked(this)
        }
    }

    override fun bindProfile(businessName: String, initials: String, emailDisplay: String) {
        setTextViewText(R.id.textviewHeaderBusiness, businessName)
        setTextViewText(R.id.textviewProfileHeroBusinessName, businessName)
        setTextViewText(R.id.textviewProfileHeroInitials, initials)
        setTextViewText(R.id.textviewProfileRowBusinessValue, businessName)
        setTextViewText(R.id.textviewProfileRowEmailValue, emailDisplay)
    }

    override fun bindProfileLiveStats(members: String, returnOrPlaceholder: String, activePromotions: String) {
        setTextViewText(R.id.textviewProfileStatMembers, members)
        setTextViewText(R.id.textviewProfileStatReturn, returnOrPlaceholder)
        setTextViewText(R.id.textviewProfileStatPromos, activePromotions)
    }

    override fun showComingSoon() {
        toastComingSoon()
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateToLoginClearTask() {
        startActivityClearTask(LoginActivity::class.java)
        finish()
    }
}
