package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.data.repository.AuthStore

class CustomersModel(private val authStore: AuthStore) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)
}
