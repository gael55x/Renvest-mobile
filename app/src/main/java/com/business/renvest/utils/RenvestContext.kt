package com.business.renvest.utils

import android.content.Context
import com.business.renvest.app.RenvestApp
import com.business.renvest.data.repository.AuthRepository

fun Context.requireRenvestApp(): RenvestApp =
    applicationContext as? RenvestApp ?: error("Application is not RenvestApp")

fun Context.authRepository(): AuthRepository = requireRenvestApp().authRepository
