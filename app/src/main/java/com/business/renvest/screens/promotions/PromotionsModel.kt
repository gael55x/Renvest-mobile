package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.entity.PromotionEntity
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore

class PromotionsModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadPromotions(): List<PromotionItem> =
        db.promotionDao().listAll().map { it.toPromotionItem() }
}

private fun PromotionEntity.toPromotionItem(): PromotionItem {
    val parsedStatus = try {
        PromotionStatus.valueOf(status)
    } catch (_: IllegalArgumentException) {
        PromotionStatus.Paused
    }
    return PromotionItem(
        id = id,
        title = title,
        reward = reward,
        expiry = expiry,
        enrolledSummary = enrolledSummary,
        usageSummary = usageSummary,
        progressPercent = progressPercent,
        status = parsedStatus,
        useGiftIcon = useGiftIcon,
    )
}
