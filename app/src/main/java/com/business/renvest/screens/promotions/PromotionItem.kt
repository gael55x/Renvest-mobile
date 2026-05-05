package com.business.renvest.screens.promotions

enum class PromotionStatus {
    Active,
    Paused,
}

data class PromotionItem(
    val id: String,
    val title: String,
    val reward: String,
    val expiry: String,
    val enrolledSummary: String,
    val usageSummary: String,
    val progressPercent: Int,
    val status: PromotionStatus,
    val useGiftIcon: Boolean,
)
