package com.business.renvest.screens.loyalty

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class LoyaltyActivity : AppCompatActivity(), LoyaltyContract.View {

    private lateinit var presenter: LoyaltyPresenter
    private lateinit var programsAdapter: LoyaltyProgramsAdapter
    private lateinit var remindersAdapter: LoyaltyRemindersAdapter
    private lateinit var textviewEmptyPrograms: TextView
    private lateinit var textviewEmptyReminders: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_loyalty, R.id.root)

        textviewEmptyPrograms = findViewById(R.id.textviewEmptyPrograms)
        textviewEmptyReminders = findViewById(R.id.textviewEmptyReminders)

        programsAdapter = LoyaltyProgramsAdapter { position ->
            presenter.onProgramLongClicked(this, position)
        }
        findViewById<RecyclerView>(R.id.recyclerviewLoyaltyPrograms).apply {
            layoutManager = LinearLayoutManager(this@LoyaltyActivity)
            adapter = programsAdapter
        }

        remindersAdapter = LoyaltyRemindersAdapter(
            onClick = { position -> presenter.onReminderClicked(this, position) },
            onLongClick = { position -> presenter.onReminderLongClicked(this, position) },
        )
        findViewById<RecyclerView>(R.id.recyclerviewLoyaltyReminders).apply {
            layoutManager = LinearLayoutManager(this@LoyaltyActivity)
            adapter = remindersAdapter
        }

        presenter = LoyaltyPresenter(this, LoyaltyModel(authStore(), renvestDb()), lifecycleScope)
        presenter.onViewReady(this)

        findViewById<MaterialButton>(R.id.buttonAddProgram).setOnClickListener {
            presenter.onAddProgramClicked(this)
        }
        findViewById<MaterialButton>(R.id.buttonAddReminder).setOnClickListener {
            presenter.onAddReminderClicked(this)
        }
    }

    override fun setupBottomNav(selectedItemId: Int, activityBadgeCount: Int) {
        setupMainBottomNavigation(selectedItemId, activityBadgeCount, clearTabSelection = true)
    }

    override fun bindHeader(businessName: String) {
        findViewById<TextView>(R.id.textviewHeaderBusiness).text = businessName
    }

    override fun bindProgramsList(items: List<LoyaltyProgramRow>) {
        programsAdapter.submitList(items)
    }

    override fun setProgramsEmptyVisible(visible: Boolean) {
        textviewEmptyPrograms.isVisible = visible
    }

    override fun bindRemindersList(items: List<LoyaltyReminderRow>) {
        remindersAdapter.submitList(items)
    }

    override fun setRemindersEmptyVisible(visible: Boolean) {
        textviewEmptyReminders.isVisible = visible
    }

    override fun showMessage(message: String) {
        toast(message)
    }

    override fun showAddProgramDialog(onSubmit: (String, Int, String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_loyalty_program, null, false)
        val nameField = dialogView.findViewById<TextInputEditText>(R.id.edittextProgramName)
        val visitsField = dialogView.findViewById<TextInputEditText>(R.id.edittextProgramVisits)
        val rewardField = dialogView.findViewById<TextInputEditText>(R.id.edittextProgramReward)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_add_loyalty_program_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                val visits = visitsField.text?.toString()?.toIntOrNull() ?: 0
                onSubmit(
                    nameField.text?.toString().orEmpty(),
                    visits,
                    rewardField.text?.toString().orEmpty(),
                )
            }
            .show()
    }

    override fun showAddReminderDialog(onSubmit: (String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_loyalty_reminder, null, false)
        val titleField = dialogView.findViewById<TextInputEditText>(R.id.edittextReminderTitle)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_add_loyalty_reminder_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(titleField.text?.toString().orEmpty())
            }
            .show()
    }
}
