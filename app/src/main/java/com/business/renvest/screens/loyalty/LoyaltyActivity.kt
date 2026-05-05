package com.business.renvest.screens.loyalty

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class LoyaltyActivity : AppCompatActivity(), LoyaltyContract.View {

    private lateinit var presenter: LoyaltyPresenter
    private lateinit var remindersAdapter: LoyaltyRemindersListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_loyalty_list_demo, R.id.root)

        presenter = LoyaltyPresenter(this, LoyaltyModel())
        presenter.onViewReady()

        val listView = findViewById<ListView>(R.id.listviewLoyaltyReminders)
        listView.setOnItemClickListener { _, _, position, _ ->
            presenter.onReminderClicked(this, position)
        }
        listView.setOnItemLongClickListener { _, _, position, _ ->
            presenter.onReminderLongClicked(this, position)
        }

        findViewById<MaterialButton>(R.id.buttonAddReminder).setOnClickListener {
            val edit = findViewById<TextInputLayout>(R.id.textinputReminderLayout).editText
                ?: return@setOnClickListener
            presenter.onAddReminderClicked(this, edit.text?.toString().orEmpty())
            edit.text?.clear()
        }
    }

    override fun setStubTitle(titleResId: Int) {
        findViewById<TextView>(R.id.textviewStubTitle).setText(titleResId)
    }

    override fun bindRemindersList(items: ArrayList<LoyaltyReminderRow>) {
        remindersAdapter = LoyaltyRemindersListAdapter(this, items)
        findViewById<ListView>(R.id.listviewLoyaltyReminders).adapter = remindersAdapter
    }

    override fun refreshRemindersList() {
        remindersAdapter.notifyDataSetChanged()
    }

    override fun showMessage(message: String) {
        toast(message)
    }
}
