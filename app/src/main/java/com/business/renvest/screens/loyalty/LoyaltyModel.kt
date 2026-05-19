package com.business.renvest.screens.loyalty

import android.content.Context
import androidx.annotation.StringRes
import com.business.renvest.R
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.LoyaltyProgramEntity
import com.business.renvest.data.local.entity.LoyaltyReminderEntity
import com.business.renvest.data.local.logActivity
import com.business.renvest.data.repository.AuthStore
import java.util.UUID

class LoyaltyModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    private val reminderDao get() = db.loyaltyReminderDao()
    private val programDao get() = db.loyaltyProgramDao()

    @StringRes
    fun screenTitleRes(): Int = R.string.loyalty_screen_title

    fun programsSnapshot(): List<LoyaltyProgramRow> =
        programDao.listAll().map { it.toProgramRow() }

    fun remindersSnapshot(): ArrayList<LoyaltyReminderRow> =
        ArrayList(reminderDao.listAll().map { it.toRow() })

    fun reminderAt(index: Int): LoyaltyReminderRow? =
        reminderDao.listAll().getOrNull(index)?.toRow()

    fun addProgram(name: String, visitsRequired: Int, rewardDescription: String): Boolean {
        val programName = name.trim()
        val reward = rewardDescription.trim()
        if (programName.isEmpty() || reward.isEmpty() || visitsRequired < 1) return false
        val now = System.currentTimeMillis()
        programDao.insert(
            LoyaltyProgramEntity(
                id = UUID.randomUUID().toString(),
                name = programName,
                visitsRequired = visitsRequired,
                rewardDescription = reward,
                createdAt = now,
                updatedAt = now,
            ),
        )
        db.logActivity(title = "Loyalty program created", subtitle = programName)
        return true
    }

    fun removeProgramAt(index: Int): LoyaltyProgramRow? {
        val entity = programDao.listAll().getOrNull(index) ?: return null
        programDao.deleteById(entity.id)
        return entity.toProgramRow()
    }

    fun addReminderFromTitle(raw: String): Boolean {
        val title = raw.trim()
        if (title.isEmpty()) return false
        val now = System.currentTimeMillis()
        reminderDao.insert(
            LoyaltyReminderEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                subtitle = NEW_REMINDER_SUBTITLE,
                createdAt = now,
                updatedAt = now,
            ),
        )
        return true
    }

    fun removeReminderAt(index: Int): LoyaltyReminderRow? {
        val entity = reminderDao.listAll().getOrNull(index) ?: return null
        reminderDao.deleteById(entity.id)
        return entity.toRow()
    }

    fun markOnboardingLoyaltyStep(context: Context) {
        authStore.markOnboardingStep(context, AuthStore.STEP_LOYALTY)
    }

    private fun LoyaltyProgramEntity.toProgramRow(): LoyaltyProgramRow =
        LoyaltyProgramRow(
            id = id,
            name = name,
            visitsRequired = visitsRequired,
            rewardDescription = rewardDescription,
        )

    private fun LoyaltyReminderEntity.toRow(): LoyaltyReminderRow =
        LoyaltyReminderRow(id = id, title = title, subtitle = subtitle)

    companion object {
        private const val NEW_REMINDER_SUBTITLE = "Long-press to remove"
    }
}
