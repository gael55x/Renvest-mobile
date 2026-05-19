package com.business.renvest.screens.loyalty

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoyaltyActivity : AppCompatActivity(), LoyaltyContract.View {

    private lateinit var presenter: LoyaltyPresenter
    private lateinit var remindersAdapter: LoyaltyRemindersListAdapter
    private lateinit var programsAdapter: LoyaltyProgramsAdapter
    private lateinit var listviewLoyaltyReminders: ListView
    private lateinit var textviewEmptyPrograms: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_loyalty, R.id.root)

        listviewLoyaltyReminders = findViewById(R.id.listviewLoyaltyReminders)
        listviewLoyaltyReminders.emptyView = findViewById(R.id.textviewEmptyReminders)
        textviewEmptyPrograms = findViewById(R.id.textviewEmptyPrograms)

        programsAdapter = LoyaltyProgramsAdapter { position ->
            presenter.onProgramLongClicked(this, position)
        }
        findViewById<RecyclerView>(R.id.recyclerviewLoyaltyPrograms).apply {
            layoutManager = LinearLayoutManager(this@LoyaltyActivity)
            adapter = programsAdapter
        }

        presenter = LoyaltyPresenter(this, LoyaltyModel(authStore(), renvestDb()), lifecycleScope)
        presenter.onViewReady(this)

        listviewLoyaltyReminders.setOnItemClickListener { _, _, position, _ ->
            presenter.onReminderClicked(this, position)
        }
        listviewLoyaltyReminders.setOnItemLongClickListener { _, _, position, _ ->
            presenter.onReminderLongClicked(this, position)
        }

        findViewById<MaterialButton>(R.id.buttonAddProgram).setOnClickListener {
            presenter.onAddProgramClicked(this)
        }

        val textinputReminderLayout = findViewById<TextInputLayout>(R.id.textinputReminderLayout)
        findViewById<MaterialButton>(R.id.buttonAddReminder).setOnClickListener {
            presenter.onAddReminderClicked(this, textinputReminderLayout.valueText(trim = false))
            textinputReminderLayout.editText?.text?.clear()
        }
    }

    override fun setScreenTitle(titleResId: Int) {
        findViewById<TextView>(R.id.textviewStubTitle).setText(titleResId)
    }

    override fun bindProgramsList(items: List<LoyaltyProgramRow>) {
        programsAdapter.submitList(items)
    }

    override fun setProgramsEmptyVisible(visible: Boolean) {
        textviewEmptyPrograms.isVisible = visible
    }

    override fun bindRemindersList(items: ArrayList<LoyaltyReminderRow>) {
        remindersAdapter = LoyaltyRemindersListAdapter(this, items)
        listviewLoyaltyReminders.adapter = remindersAdapter
    }

    override fun refreshRemindersList() {
        remindersAdapter.notifyDataSetChanged()
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
}
