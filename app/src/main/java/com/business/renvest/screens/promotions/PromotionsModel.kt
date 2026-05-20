package com.business.renvest.screens.promotions

import android.content.Context
import com.business.renvest.data.local.ActivityEventType
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.PromotionEntity
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.local.logActivity
import com.business.renvest.data.local.withMetrics
import com.business.renvest.data.repository.AuthStore
import java.util.UUID

class PromotionsModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadPromotions(filter: PromoFilter = PromoFilter.ALL): List<PromotionItem> =
        db.promotionDao().listAll()
            .map { it.toPromotionItem() }
            .filter { item ->
                when (filter) {
                    PromoFilter.ALL -> true
                    PromoFilter.ACTIVE -> item.status == PromotionStatus.Active
                    PromoFilter.PAUSED -> item.status == PromotionStatus.Paused
                }
            }

    fun addPromotion(
        context: Context,
        title: String,
        reward: String,
        expiry: String,
        enrolledCount: Int,
        redeemedCount: Int,
    ): Boolean {
        val t = title.trim()
        val r = reward.trim()
        val e = expiry.trim()
        if (t.isEmpty() || r.isEmpty() || e.isEmpty()) return false
        val now = System.currentTimeMillis()
        val entity = PromotionEntity(
            id = UUID.randomUUID().toString(),
            title = t,
            reward = r,
            expiry = e,
            enrolledSummary = "",
            usageSummary = "",
            enrolledCount = 0,
            redeemedCount = 0,
            progressPercent = 0,
            status = PromotionStatus.Active.name,
            useGiftIcon = false,
            createdAt = now,
            updatedAt = now,
        ).withMetrics(enrolledCount, redeemedCount, context)
        db.promotionDao().insert(entity)
        db.logActivity(
            title = "Promotion created",
            subtitle = t,
            eventType = ActivityEventType.SYSTEM,
        )
        return true
    }

    fun updatePromotion(
        context: Context,
        item: PromotionItem,
        title: String,
        reward: String,
        expiry: String,
        enrolledCount: Int,
        redeemedCount: Int,
    ): Boolean {
        val t = title.trim()
        val r = reward.trim()
        val e = expiry.trim()
        if (t.isEmpty() || r.isEmpty() || e.isEmpty()) return false
        val existing = db.promotionDao().listAll().find { it.id == item.id } ?: return false
        val now = System.currentTimeMillis()
        val updated = existing.copy(
            title = t,
            reward = r,
            expiry = e,
            updatedAt = now,
        ).withMetrics(enrolledCount, redeemedCount, context)
        db.promotionDao().update(updated)
        return true
    }

    fun recordRedemption(context: Context, item: PromotionItem): Boolean {
        val existing = db.promotionDao().listAll().find { it.id == item.id } ?: return false
        if (existing.enrolledCount <= 0) return false
        val nextRedeemed = (existing.redeemedCount + 1).coerceAtMost(existing.enrolledCount)
        val updated = existing.withMetrics(existing.enrolledCount, nextRedeemed, context)
        db.promotionDao().update(updated)
        db.logActivity(
            title = "Promotion redeemed",
            subtitle = existing.title,
            eventType = ActivityEventType.SYSTEM,
        )
        return true
    }

    fun deletePromotion(id: String) {
        db.promotionDao().deleteById(id)
    }

    fun togglePromotionStatus(id: String, current: PromotionStatus): Boolean {
        val next = if (current == PromotionStatus.Active) PromotionStatus.Paused else PromotionStatus.Active
        val ok = db.promotionDao().updateStatus(id, next.name, System.currentTimeMillis()) > 0
        if (ok) {
            db.logActivity(
                title = if (next == PromotionStatus.Active) "Promotion resumed" else "Promotion paused",
                subtitle = db.promotionDao().listAll().find { it.id == id }?.title.orEmpty(),
                eventType = ActivityEventType.SYSTEM,
            )
        }
        return ok
    }

    fun markOnboardingPromotionStep(context: Context) {
        authStore.markOnboardingStep(context, AuthStore.STEP_PROMOTION)
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
        enrolledCount = enrolledCount,
        redeemedCount = redeemedCount,
        progressPercent = progressPercent,
        status = parsedStatus,
        useGiftIcon = useGiftIcon,
    )
}
