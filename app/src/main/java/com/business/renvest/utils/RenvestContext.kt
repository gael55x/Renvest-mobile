package com.business.renvest.utils

import android.content.Context
import com.business.renvest.app.RenvestApp
import com.business.renvest.data.repository.AuthStore

fun Context.requireRenvestApp(): RenvestApp =
    applicationContext as? RenvestApp ?: error("Application is not RenvestApp")

fun Context.authStore(): AuthStore = requireRenvestApp().authStore

fun Context.displayBusinessName(): String = authStore().businessDisplayName(this)
