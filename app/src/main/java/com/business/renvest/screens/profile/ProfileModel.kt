package com.business.renvest.screens.profile

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.LocalDataExporter
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore
import java.io.File

class ProfileModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun getEmail(context: Context): String = authStore.getEmail(context)

    fun getOwnerName(context: Context): String = authStore.getOwnerName(context)

    fun getBusinessType(context: Context): String = authStore.getBusinessType(context)

    fun getBusinessLocation(context: Context): String = authStore.getBusinessLocation(context)

    fun getLoyaltyRewardThreshold(context: Context): Int = authStore.getLoyaltyRewardThreshold(context)

    fun getLoyaltyPointsMode(context: Context): String = authStore.getLoyaltyPointsMode(context)

    fun loyaltyThresholdDisplay(context: Context): String {
        val points = getLoyaltyRewardThreshold(context)
        return if (points > 0) {
            context.getString(R.string.profile_loyalty_threshold_points_format, points)
        } else {
            context.getString(R.string.profile_value_not_set)
        }
    }

    fun loyaltyPointsModeDisplay(context: Context): String = when (getLoyaltyPointsMode(context)) {
        LoyaltyPointsMode.PER_VISIT -> context.getString(R.string.loyalty_mode_per_visit)
        LoyaltyPointsMode.PER_SPEND -> context.getString(R.string.loyalty_mode_per_spend)
        LoyaltyPointsMode.MANUAL -> context.getString(R.string.loyalty_mode_manual)
        else -> context.getString(R.string.profile_value_not_set)
    }

    fun updateLoyaltyRewardThreshold(context: Context, rawPoints: String): RenvestResult<Unit> {
        val points = rawPoints.trim().toIntOrNull()
            ?: return RenvestResult.Err.Validation(context.getString(R.string.error_loyalty_threshold_invalid))
        if (points !in 1..99_999) {
            return RenvestResult.Err.Validation(context.getString(R.string.error_loyalty_threshold_invalid))
        }
        return authStore.updateLoyaltyRewardThreshold(context, points)
    }

    fun updateLoyaltyPointsMode(context: Context, mode: String): RenvestResult<Unit> =
        authStore.updateLoyaltyPointsMode(context, mode)

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun clearSession(context: Context): RenvestResult<Unit> = authStore.clearSession(context)

    fun updateBusinessProfile(
        context: Context,
        businessName: String,
        businessType: String,
        location: String,
    ): RenvestResult<Unit> = authStore.updateBusinessProfile(context, businessName, businessType, location)

    fun exportLocalData(context: Context): File = LocalDataExporter(db).exportToCacheFile(context)

    fun initialsFromName(name: String): String {
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        return when {
            parts.isEmpty() -> "RV"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts.last().first()}".uppercase()
        }
    }
}
