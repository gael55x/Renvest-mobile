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
            presenter.onBusinessNameClicked(this)
        }

        findViewById<View>(R.id.rowLoyaltyThreshold).setOnClickListener {
            presenter.onLoyaltyThresholdClicked(this)
        }
        findViewById<View>(R.id.rowLoyaltyPointsMode).setOnClickListener {
            presenter.onLoyaltyPointsModeClicked(this)
        }

        findViewById<View>(R.id.rowSettingsBusinessType).setOnClickListener {
            presenter.onBusinessTypeClicked(this)
        }
        findViewById<View>(R.id.rowSettingsLocation).setOnClickListener {
            presenter.onLocationClicked(this)
        }
        findViewById<View>(R.id.rowSettingsEmail).setOnClickListener {
            presenter.onEmailClicked(this)
        }

        findViewById<MaterialButton>(R.id.buttonLogout).setOnClickListener {
            presenter.onLogoutClicked(this)
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

    override fun bindBusinessSettings(businessType: String, location: String) {
        setTextViewText(R.id.textviewProfileRowBusinessTypeValue, businessType)
        setTextViewText(R.id.textviewProfileRowLocationValue, location)
    }

    override fun bindProfileLiveStats(members: String, returnOrPlaceholder: String, activePromotions: String) {
        setTextViewText(R.id.textviewProfileStatMembers, members)
        setTextViewText(R.id.textviewProfileStatReturn, returnOrPlaceholder)
        setTextViewText(R.id.textviewProfileStatPromos, activePromotions)
    }

    override fun bindLoyaltySettings(thresholdLabel: String, pointsModeLabel: String) {
        setTextViewText(R.id.textviewProfileLoyaltyThresholdChip, thresholdLabel)
        setTextViewText(R.id.textviewProfileLoyaltyPointsModeChip, pointsModeLabel)
    }

    override fun showLoyaltyThresholdDialog(currentPoints: Int, onSubmit: (String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loyalty_threshold, null, false)
        val input = dialogView.findViewById<TextInputEditText>(R.id.edittextLoyaltyThreshold)
        if (currentPoints > 0) {
            input.setText(currentPoints.toString())
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_loyalty_threshold_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(input.text?.toString().orEmpty())
            }
            .show()
    }

    override fun showLoyaltyPointsModeDialog(
        checkedIndex: Int,
        options: Array<String>,
        onSelected: (Int) -> Unit,
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_loyalty_points_mode_title)
            .setSingleChoiceItems(options, checkedIndex) { dialog, which ->
                onSelected(which)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun showLocalDataDisclaimer() = Unit

    override fun showEditBusinessDialog(
        field: BusinessEditField,
        businessName: String,
        businessType: String,
        location: String,
        onSubmit: (String, String, String) -> Unit,
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_business, null, false)
        val nameLayout = dialogView.findViewById<View>(R.id.textinputBusinessName)
        val typeLayout = dialogView.findViewById<View>(R.id.textinputBusinessType)
        val locationLayout = dialogView.findViewById<View>(R.id.textinputBusinessLocation)
        dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessName).setText(businessName)
        dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessType).setText(businessType)
        dialogView.findViewById<TextInputEditText>(R.id.edittextBusinessLocation).setText(location)

        val titleRes = when (field) {
            BusinessEditField.NAME -> R.string.dialog_edit_business_name_title
            BusinessEditField.TYPE -> R.string.dialog_edit_business_type_title
            BusinessEditField.LOCATION -> R.string.dialog_edit_business_location_title
            BusinessEditField.ALL -> R.string.dialog_edit_business_title
        }
        when (field) {
            BusinessEditField.NAME -> {
                typeLayout.visibility = View.GONE
                locationLayout.visibility = View.GONE
            }
            BusinessEditField.TYPE -> {
                nameLayout.visibility = View.GONE
                locationLayout.visibility = View.GONE
            }
            BusinessEditField.LOCATION -> {
                nameLayout.visibility = View.GONE
                typeLayout.visibility = View.GONE
            }
            BusinessEditField.ALL -> Unit
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(titleRes)
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
