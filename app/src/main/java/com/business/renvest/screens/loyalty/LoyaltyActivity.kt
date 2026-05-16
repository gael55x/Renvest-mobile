package com.business.renvest.screens.loyalty

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class LoyaltyActivity : AppCompatActivity(), LoyaltyContract.View {

    private lateinit var presenter: LoyaltyPresenter
    private lateinit var remindersAdapter: LoyaltyRemindersListAdapter
    private lateinit var listviewLoyaltyReminders: ListView
    private lateinit var textviewStubTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_loyalty, R.id.root)

        listviewLoyaltyReminders = findViewById(R.id.listviewLoyaltyReminders)
        listviewLoyaltyReminders.emptyView = findViewById(R.id.textviewEmptyReminders)
        textviewStubTitle = findViewById(R.id.textviewStubTitle)

        presenter = LoyaltyPresenter(this, LoyaltyModel(renvestDb()))
        presenter.onViewReady()

        listviewLoyaltyReminders.setOnItemClickListener { _, _, position, _ ->
            presenter.onReminderClicked(this, position)
        }
        listviewLoyaltyReminders.setOnItemLongClickListener { _, _, position, _ ->
            presenter.onReminderLongClicked(this, position)
        }

        val materialbuttonAddReminder = findViewById<MaterialButton>(R.id.buttonAddReminder)
        val textinputReminderLayout = findViewById<TextInputLayout>(R.id.textinputReminderLayout)
        materialbuttonAddReminder.setOnClickListener {
            val edit = textinputReminderLayout.editText
                ?: return@setOnClickListener
            presenter.onAddReminderClicked(this, textinputReminderLayout.valueText(trim = false))
            edit.text?.clear()
        }
    }

    override fun setStubTitle(titleResId: Int) {
        textviewStubTitle.setText(titleResId)
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
}
