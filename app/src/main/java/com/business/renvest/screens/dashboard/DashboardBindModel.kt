package com.business.renvest.screens.dashboard

data class DashboardBindModel(
    val avatarInitials: String,
    val revenueValue: String,
    val revenueSubline: String,
    val visitsValue: String,
    val membersValue: String,
    val returnValue: String,
    val perfMembers: String,
    val perfMembersTrendVisible: Boolean,
    val perfLoyaltyPrograms: String,
    val perfPromotions: String,
    val perfPromotionsTrendVisible: Boolean,
    val perfActivityEvents: String,
    val aiTitle: String,
    val aiBody: String,
)
