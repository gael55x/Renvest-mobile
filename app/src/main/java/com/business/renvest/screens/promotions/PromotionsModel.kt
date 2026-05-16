package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.PromotionEntity
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore
import java.util.UUID

class PromotionsModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadPromotions(): List<PromotionItem> =
        db.promotionDao().listAll().map { it.toPromotionItem() }

    /** All three fields required after trim. */
    fun addPromotionMinimal(context: Context, title: String, reward: String, expiry: String): Boolean {
        val t = title.trim()
        val r = reward.trim()
        val e = expiry.trim()
        if (t.isEmpty() || r.isEmpty() || e.isEmpty()) return false
        val now = System.currentTimeMillis()
        val placeholder = context.getString(R.string.metric_em_dash)
        db.promotionDao().insert(
            PromotionEntity(
                id = UUID.randomUUID().toString(),
                title = t,
                reward = r,
                expiry = e,
                enrolledSummary = placeholder,
                usageSummary = placeholder,
                progressPercent = 0,
                status = PromotionStatus.Active.name,
                useGiftIcon = false,
                createdAt = now,
                updatedAt = now,
            ),
        )
        return true
    }

    fun togglePromotionStatus(id: String, current: PromotionStatus): Boolean {
        val next = if (current == PromotionStatus.Active) PromotionStatus.Paused else PromotionStatus.Active
        return db.promotionDao().updateStatus(id, next.name, System.currentTimeMillis()) > 0
    }
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
