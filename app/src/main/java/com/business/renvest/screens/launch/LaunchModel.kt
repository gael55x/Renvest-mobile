package com.business.renvest.screens.launch

import android.content.Context
import com.business.renvest.data.repository.AuthStore

class LaunchModel(private val authStore: AuthStore) {

    fun isLoggedIn(context: Context): Boolean = authStore.isLoggedIn(context)
}
