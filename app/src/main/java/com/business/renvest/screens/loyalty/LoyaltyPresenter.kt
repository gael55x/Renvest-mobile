package com.business.renvest.screens.loyalty

import android.content.Context
import com.business.renvest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoyaltyPresenter(
    private val view: LoyaltyContract.View,
    private val model: LoyaltyModel,
    private val scope: CoroutineScope,
) : LoyaltyContract.Presenter {

    override fun onViewReady(context: Context) {
        bindScreen(context)
    }

    override fun onAddProgramClicked(context: Context) {
        view.showAddProgramDialog { name, visits, reward ->
            onAddProgramSubmitted(context, name, visits, reward)
        }
    }

    override fun onAddProgramSubmitted(context: Context, name: String, visits: Int, reward: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.addProgram(name, visits, reward) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    model.markOnboardingLoyaltyStep(context)
                    bindScreen(context)
                    view.showMessage(context.getString(R.string.loyalty_program_added))
                } else {
                    view.showMessage(context.getString(R.string.loyalty_program_invalid))
                }
            }
        }
    }

    override fun onProgramLongClicked(context: Context, position: Int): Boolean {
        scope.launch {
            val removed = withContext(Dispatchers.IO) { model.removeProgramAt(position) }
            withContext(Dispatchers.Main) {
                if (removed != null) {
                    bindScreen(context)
                    view.showMessage(
                        context.getString(R.string.loyalty_program_removed_format, removed.name),
                    )
                }
            }
        }
        return true
    }

    override fun onReminderClicked(context: Context, position: Int) {
        scope.launch {
            val item = withContext(Dispatchers.IO) { model.reminderAt(position) } ?: return@launch
            withContext(Dispatchers.Main) {
                view.showMessage(
                    context.getString(R.string.loyalty_reminder_item_click_format, item.title, item.subtitle),
                )
            }
        }
    }

    override fun onReminderLongClicked(context: Context, position: Int): Boolean {
        scope.launch {
            val removed = withContext(Dispatchers.IO) { model.removeReminderAt(position) }
            withContext(Dispatchers.Main) {
                if (removed != null) {
                    bindReminders(context)
                    view.showMessage(
                        context.getString(R.string.loyalty_reminder_removed_format, removed.title),
                    )
                }
            }
        }
        return true
    }

    override fun onAddReminderClicked(context: Context, rawTitle: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.addReminderFromTitle(rawTitle) }
            withContext(Dispatchers.Main) {
                if (!ok) {
                    view.showMessage(context.getString(R.string.loyalty_reminder_empty_error))
                    return@withContext
                }
                bindReminders(context)
            }
        }
    }

    private fun bindScreen(context: Context) {
        view.setScreenTitle(model.screenTitleRes())
        scope.launch {
            val programs = withContext(Dispatchers.IO) { model.programsSnapshot() }
            val reminders = withContext(Dispatchers.IO) { model.remindersSnapshot() }
            withContext(Dispatchers.Main) {
                view.bindProgramsList(programs)
                view.setProgramsEmptyVisible(programs.isEmpty())
                view.bindRemindersList(reminders)
            }
        }
    }

    private fun bindReminders(context: Context) {
        scope.launch {
            val reminders = withContext(Dispatchers.IO) { model.remindersSnapshot() }
            withContext(Dispatchers.Main) {
                view.bindRemindersList(reminders)
                view.refreshRemindersList()
            }
        }
    }
}
