package com.business.renvest.utils

import android.content.Context
import com.business.renvest.app.RenvestApp
import com.business.renvest.data.repository.AuthRepository
import com.business.renvest.data.repository.businessDisplayName

fun Context.requireRenvestApp(): RenvestApp =
    applicationContext as? RenvestApp ?: error("Application is not RenvestApp")

fun Context.authRepository(): AuthRepository = requireRenvestApp().authRepository

fun Context.displayBusinessName(): String = authRepository().businessDisplayName(this)
