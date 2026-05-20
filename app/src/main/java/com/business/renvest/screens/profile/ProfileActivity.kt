package com.business.renvest.screens.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.io.File

class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_profile, R.id.root)

        presenter = ProfilePresenter(this, ProfileModel(authStore(), renvestDb()), lifecycleScope)
        presenter.onViewReady(this)

        findViewById<View>(R.id.buttonProfileOverflow).setOnClickListener {
            presenter.onExportClicked(this)
        }
        findViewById<View>(R.id.buttonProfileHeroEdit).setOnClickListener {
            presenter.onEditBusinessClicked(this)
        }
        findViewById<View>(R.id.rowSettingsBusinessName).setOnClickListener {
            presenter.onEditBusinessClicked(this)
        }

        val stub = View.OnClickListener { presenter.onSettingsStubClicked() }
        setClickListeners(
            stub,
            R.id.rowSettingsBusinessType,
            R.id.rowSettingsEmail,
            R.id.rowSettingsLocation,
            R.id.rowLoyaltyThreshold,
            R.id.rowLoyaltyPointsMode,
        )

        findViewById<MaterialButton>(R.id.buttonLogout).setOnClickListener {
            presenter.onLogoutClicked()
        }
    }

    override fun setupBottomNav(selectedItemId: Int, activityBadgeCount: Int) {
        setupMainBottomNavigation(selectedItemId, activityBadgeCount)
    }

    override fun bindProfile(businessName: String, initials: String, emailDisplay: String, ownerLine: String) {
        setTextViewText(R.id.textviewHeaderBusiness, businessName)
        setTextViewText(R.id.textviewProfileHeroBusinessName, businessName)
        setTextViewText(R.id.textviewProfileHeroInitials, initials)
        setTextViewText(R.id.textviewProfileRowBusinessValue, businessName)
        setTextViewText(R.id.textviewProfileRowEmailValue, emailDisplay)
        val subtitle = findViewById<TextView>(R.id.textviewProfileHeroSubtitle)
        if (ownerLine.isNotEmpty()) {
            subtitle.text = ownerLine
            subtitle.visibility = View.VISIBLE
        } else {
            subtitle.visibility = View.GONE
        }
    }

    override fun bindProfileLiveStats(members: String, returnOrPlaceholder: String, activePromotions: String) {
        setTextViewText(R.id.textviewProfileStatMembers, members)
        setTextViewText(R.id.textviewProfileStatReturn, returnOrPlaceholder)
        setTextViewText(R.id.textviewProfileStatPromos, activePromotions)
    }

    override fun showLocalDataDisclaimer() = Unit

    override fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_logout_title)
            .setMessage(R.string.dialog_logout_message)
            .setNegativeButton(R.string.logout_keep_data, null)
            .setNeutralButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.logout_clear_data) { _, _ ->
                presenter.onLogoutConfirmed(this, clearLocalData = true)
            }
            .create()
            .apply {
                setOnShowListener {
                    getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
                        presenter.onLogoutConfirmed(this@ProfileActivity, clearLocalData = false)
                        dismiss()
                    }
                }
            }
            .show()
    }

    override fun showEditBusinessDialog(
        businessName: String,
        businessType: String,
        location: String,
        onSubmit: (String, String, String) -> Unit,
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_business, null, false)
        dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessName).setText(businessName)
        dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessType).setText(businessType)
        dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessLocation).setText(location)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_edit_business_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(
                    dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessName).text?.toString().orEmpty(),
                    dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessType).text?.toString().orEmpty(),
                    dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessLocation).text?.toString().orEmpty(),
                )
            }
            .show()
    }

    override fun shareExportFile(file: File) {
        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        startActivity(
            Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            },
        )
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
