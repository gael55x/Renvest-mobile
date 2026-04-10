package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthRepository

class PromotionsModel(private val authRepository: AuthRepository) {
    fun headerBusinessName(context: Context): String {
        val stored = authRepository.getBusinessName(context).trim()
        return if (stored.isNotEmpty()) stored else context.getString(R.string.default_business_display)
    }
}
