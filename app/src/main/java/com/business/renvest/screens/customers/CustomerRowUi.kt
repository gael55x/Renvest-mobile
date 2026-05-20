package com.business.renvest.screens.customers

data class CustomerRowUi(
    val id: String,
    val displayName: String,
    val progressSummary: String = "",
    val lastVisitSummary: String = "",
)
