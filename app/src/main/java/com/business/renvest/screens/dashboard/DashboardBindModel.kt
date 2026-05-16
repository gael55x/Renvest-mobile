package com.business.renvest.screens.dashboard

/**
 * Values bound to the dashboard hero, performance tiles, and AI preview card.
 * All strings are formatted in the presenter so the view stays passive.
 */
data class DashboardBindModel(
    val avatarInitials: String,
    val revenueValue: String,
    val revenueSubline: String,
    val visitsValue: String,
    val membersValue: String,
    val returnValue: String,
    val perfMembers: String,
    val perfMembersTrendVisible: Boolean,
    val perfRating: String,
    val perfTicket: String,
    val perfTicketTrendVisible: Boolean,
    val perfChurn: String,
    val aiTitle: String,
    val aiBody: String,
)
