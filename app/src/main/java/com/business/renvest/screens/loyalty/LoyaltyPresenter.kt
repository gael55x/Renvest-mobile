package com.business.renvest.screens.loyalty

import android.content.Context
import com.business.renvest.R

class LoyaltyPresenter(
    private val view: LoyaltyContract.View,
    private val model: LoyaltyModel,
) : LoyaltyContract.Presenter {

    override fun onViewReady() {
        view.setStubTitle(model.stubScreenTitleRes())
        view.bindRemindersList(model.remindersSnapshot())
    }

    override fun onReminderClicked(context: Context, position: Int) {
        val item = model.reminderAt(position) ?: return
        view.showMessage(
            context.getString(R.string.loyalty_reminder_item_click_format, item.title, item.subtitle),
        )
    }

    override fun onReminderLongClicked(context: Context, position: Int): Boolean {
        val removed = model.removeReminderAt(position) ?: return false
        view.refreshRemindersList()
        view.showMessage(context.getString(R.string.loyalty_reminder_removed_format, removed.title))
        return true
    }

    override fun onAddReminderClicked(context: Context, rawTitle: String) {
        if (!model.addReminderFromTitle(rawTitle)) {
            view.showMessage(context.getString(R.string.loyalty_reminder_empty_error))
            return
        }
        view.refreshRemindersList()
    }
}
