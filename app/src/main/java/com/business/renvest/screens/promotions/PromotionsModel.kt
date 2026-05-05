package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore

class PromotionsModel(private val authStore: AuthStore) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun demoPromotions(context: Context): List<PromotionItem> {
        val c = context.applicationContext
        return listOf(
            PromotionItem(
                id = "promo_free_coffee",
                title = c.getString(R.string.promo_card_1_title),
                reward = c.getString(R.string.promo_card_1_reward),
                expiry = c.getString(R.string.promo_card_1_expiry),
                enrolledSummary = c.getString(R.string.promo_card_1_enrolled),
                usageSummary = c.getString(R.string.promo_card_1_usage),
                progressPercent = 72,
                status = PromotionStatus.Active,
                useGiftIcon = false,
            ),
            PromotionItem(
                id = "promo_birthday",
                title = c.getString(R.string.promo_card_2_title),
                reward = c.getString(R.string.promo_card_2_reward),
                expiry = c.getString(R.string.promo_card_2_expiry),
                enrolledSummary = c.getString(R.string.promo_card_2_enrolled),
                usageSummary = c.getString(R.string.promo_card_2_usage),
                progressPercent = 28,
                status = PromotionStatus.Paused,
                useGiftIcon = true,
            ),
        )
    }
}
