package com.business.renvest.screens.register

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.clearAllBusinessData
import com.business.renvest.data.repository.AuthStore

class RegisterModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun signUp(
        context: Context,
        businessName: String,
        ownerName: String,
        email: String,
        password: String,
    ): RenvestResult<Unit> {
        db.clearAllBusinessData()
        return authStore.signUp(context, businessName, ownerName, email, password)
    }
}
