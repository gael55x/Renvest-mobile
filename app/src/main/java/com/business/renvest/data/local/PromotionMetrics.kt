package com.business.renvest.data.local

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.local.entity.PromotionEntity

fun PromotionEntity.withMetrics(
    enrolled: Int,
    redeemed: Int,
    context: Context,
): PromotionEntity {
    val safeEnrolled = enrolled.coerceAtLeast(0)
    val safeRedeemed = redeemed.coerceAtLeast(0).coerceAtMost(safeEnrolled)
    val progress = if (safeEnrolled > 0) {
        ((safeRedeemed * 100) / safeEnrolled).coerceIn(0, 100)
    } else {
        0
    }
    return copy(
        enrolledCount = safeEnrolled,
        redeemedCount = safeRedeemed,
        enrolledSummary = context.getString(R.string.promo_enrolled_count_format, safeEnrolled),
        usageSummary = context.getString(R.string.promo_redeemed_count_format, safeRedeemed),
        progressPercent = progress,
    )
}
