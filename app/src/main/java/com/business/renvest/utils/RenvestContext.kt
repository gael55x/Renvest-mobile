package com.business.renvest.utils

import android.content.Context
import com.business.renvest.R
import com.business.renvest.app.RenvestApp
import com.business.renvest.data.repository.AuthRepository

fun Context.requireRenvestApp(): RenvestApp =
    applicationContext as? RenvestApp ?: error("Application is not RenvestApp")

fun Context.authRepository(): AuthRepository = requireRenvestApp().authRepository

fun Context.displayBusinessName(): String {
    val stored = authRepository().getBusinessName(this).trim()
    return if (stored.isNotEmpty()) stored else getString(R.string.default_business_display)
}
