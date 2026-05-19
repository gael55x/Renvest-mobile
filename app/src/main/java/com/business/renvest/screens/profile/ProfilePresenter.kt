package com.business.renvest.screens.profile

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilePresenter(
    private val view: ProfileContract.View,
    private val model: ProfileModel,
    private val scope: CoroutineScope,
) : ProfileContract.Presenter {

    override fun onViewReady(context: Context) {
        val business = model.businessDisplayName(context)
        val emailTrimmed = model.getEmail(context).trim()
        val emailDisplay = if (emailTrimmed.isNotEmpty()) {
            emailTrimmed
        } else {
            context.getString(R.string.profile_email_empty)
        }
        val owner = model.getOwnerName(context).trim()
        val ownerLine = if (owner.isNotEmpty()) {
            context.getString(R.string.profile_owner_format, owner)
        } else {
            context.getString(R.string.local_only_disclaimer_short)
        }
        view.bindProfile(business, model.initialsFromName(business), emailDisplay, ownerLine)
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            val notRecorded = context.getString(R.string.metric_not_recorded)
            withContext(Dispatchers.Main) {
                view.bindProfileLiveStats(
                    members = counts.customers.toString(),
                    returnOrPlaceholder = notRecorded,
                    activePromotions = counts.promotionsActive.toString(),
                )
            }
        }
        view.showLocalDataDisclaimer()
    }

    override fun onLogoutClicked() {
        view.showLogoutDialog()
    }

    override fun onLogoutConfirmed(context: Context, clearLocalData: Boolean) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                if (clearLocalData) {
                    model.clearSessionAndLocalData(context)
                } else {
                    model.clearSession(context)
                }
            }
            withContext(Dispatchers.Main) {
                when (result) {
                    is RenvestResult.Ok -> view.navigateToLoginClearTask()
                    else -> result.notifyErrorIfNotOk { view.showToast(it) }
                }
            }
        }
    }

    override fun onExportClicked(context: Context) {
        scope.launch {
            val file = withContext(Dispatchers.IO) { model.exportLocalData(context) }
            withContext(Dispatchers.Main) {
                view.shareExportFile(file)
                view.showToast(context.getString(R.string.export_saved_format, file.name))
            }
        }
    }

    override fun onEditBusinessClicked(context: Context) {
        view.showEditBusinessDialog(
            businessName = model.businessDisplayName(context),
            businessType = model.getBusinessType(context),
            location = model.getBusinessLocation(context),
        ) { name, type, location ->
            onEditBusinessSubmitted(context, name, type, location)
        }
    }

    override fun onEditBusinessSubmitted(context: Context, name: String, type: String, location: String) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                model.updateBusinessProfile(context, name, type, location)
            }
            withContext(Dispatchers.Main) {
                when (result) {
                    is RenvestResult.Ok -> {
                        onViewReady(context)
                        view.showToast(context.getString(R.string.profile_business_updated))
                    }
                    else -> result.notifyErrorIfNotOk { view.showToast(it) }
                }
            }
        }
    }

    override fun onSettingsStubClicked() {
        view.showComingSoon()
    }
}
