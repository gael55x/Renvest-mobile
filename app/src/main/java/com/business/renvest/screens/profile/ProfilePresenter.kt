package com.business.renvest.screens.profile

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk

class ProfilePresenter(
    private val view: ProfileContract.View,
    private val model: ProfileModel,
) : ProfileContract.Presenter {

    override fun onViewReady(context: Context) {
        val business = model.businessDisplayName(context)
        val emailTrimmed = model.getEmail(context).trim()
        val emailDisplay = if (emailTrimmed.isNotEmpty()) {
            emailTrimmed
        } else {
            context.getString(R.string.profile_email_placeholder)
        }
        val initials = model.initialsFromName(business)
        view.bindProfile(business, initials, emailDisplay)
    }

    override fun onLogoutClicked(context: Context) {
        when (val result = model.clearSession(context)) {
            is RenvestResult.Ok -> view.navigateToLoginClearTask()
            else -> result.notifyErrorIfNotOk { view.showToast(it) }
        }
    }

    override fun onSettingsStubClicked() {
        view.showComingSoon()
    }
}
