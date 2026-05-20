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
        val ownerLine = if (owner.isNotEmpty()) owner else ""
        view.bindProfile(business, model.initialsFromName(business), emailDisplay, ownerLine)
        view.bindBusinessSettings(
            businessType = model.businessTypeDisplay(context),
            location = model.businessLocationDisplay(context),
        )
        view.bindLoyaltySettings(
            thresholdLabel = model.loyaltyThresholdDisplay(context),
            pointsModeLabel = model.loyaltyPointsModeDisplay(context),
        )
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            val notRecorded = context.getString(R.string.metric_not_recorded)
            withContext(Dispatchers.Main) {
                view.setupBottomNav(R.id.navProfile, counts.activityEvents)
                view.bindProfileLiveStats(
                    members = counts.customers.toString(),
                    returnOrPlaceholder = notRecorded,
                    activePromotions = counts.promotionsActive.toString(),
                )
            }
        }
    }

    override fun onLogoutClicked(context: Context) {
        scope.launch {
            val result = withContext(Dispatchers.IO) { model.clearSession(context) }
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
        openBusinessEditDialog(context, BusinessEditField.ALL)
    }

    override fun onBusinessNameClicked(context: Context) {
        openBusinessEditDialog(context, BusinessEditField.NAME)
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

    override fun onLoyaltyThresholdClicked(context: Context) {
        view.showLoyaltyThresholdDialog(model.getLoyaltyRewardThreshold(context)) { raw ->
            onLoyaltyThresholdSubmitted(context, raw)
        }
    }

    override fun onLoyaltyThresholdSubmitted(context: Context, rawPoints: String) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                model.updateLoyaltyRewardThreshold(context, rawPoints)
            }
            withContext(Dispatchers.Main) {
                when (result) {
                    is RenvestResult.Ok -> {
                        view.bindLoyaltySettings(
                            thresholdLabel = model.loyaltyThresholdDisplay(context),
                            pointsModeLabel = model.loyaltyPointsModeDisplay(context),
                        )
                        view.showToast(context.getString(R.string.profile_loyalty_saved))
                    }
                    else -> result.notifyErrorIfNotOk { view.showToast(it) }
                }
            }
        }
    }

    override fun onLoyaltyPointsModeClicked(context: Context) {
        val options = LoyaltyPointsMode.ALL.map { modeLabel(context, it) }.toTypedArray()
        val checkedIndex = LoyaltyPointsMode.ALL.indexOf(model.getLoyaltyPointsMode(context))
        view.showLoyaltyPointsModeDialog(checkedIndex, options) { index ->
            onLoyaltyPointsModeSelected(context, LoyaltyPointsMode.ALL[index])
        }
    }

    override fun onLoyaltyPointsModeSelected(context: Context, mode: String) {
        scope.launch {
            val result = withContext(Dispatchers.IO) { model.updateLoyaltyPointsMode(context, mode) }
            withContext(Dispatchers.Main) {
                when (result) {
                    is RenvestResult.Ok -> {
                        view.bindLoyaltySettings(
                            thresholdLabel = model.loyaltyThresholdDisplay(context),
                            pointsModeLabel = model.loyaltyPointsModeDisplay(context),
                        )
                        view.showToast(context.getString(R.string.profile_loyalty_saved))
                    }
                    else -> result.notifyErrorIfNotOk { view.showToast(it) }
                }
            }
        }
    }

    override fun onBusinessTypeClicked(context: Context) {
        openBusinessEditDialog(context, BusinessEditField.TYPE)
    }

    override fun onLocationClicked(context: Context) {
        openBusinessEditDialog(context, BusinessEditField.LOCATION)
    }

    private fun openBusinessEditDialog(context: Context, field: BusinessEditField) {
        view.showEditBusinessDialog(
            field = field,
            businessName = model.businessDisplayName(context),
            businessType = model.getBusinessType(context),
            location = model.getBusinessLocation(context),
        ) { name, type, location ->
            onEditBusinessSubmitted(context, name, type, location)
        }
    }

    override fun onEmailClicked(context: Context) {
        val email = model.getEmail(context).trim()
        if (email.isEmpty()) {
            view.showToast(context.getString(R.string.profile_email_empty))
        } else {
            view.showToast(context.getString(R.string.profile_email_readonly_format, email))
        }
    }

    private fun modeLabel(context: Context, mode: String): String = when (mode) {
        LoyaltyPointsMode.PER_VISIT -> context.getString(R.string.loyalty_mode_per_visit)
        LoyaltyPointsMode.PER_SPEND -> context.getString(R.string.loyalty_mode_per_spend)
        LoyaltyPointsMode.MANUAL -> context.getString(R.string.loyalty_mode_manual)
        else -> mode
    }
}
