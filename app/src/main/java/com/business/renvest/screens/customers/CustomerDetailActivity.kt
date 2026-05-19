package com.business.renvest.screens.customers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.screens.activityfeed.ActivityEventRowUi
import com.business.renvest.screens.activityfeed.ActivityFeedAdapter
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class CustomerDetailActivity : AppCompatActivity(), CustomerDetailContract.View {

    private lateinit var presenter: CustomerDetailPresenter
    private lateinit var customerId: String
    private lateinit var activityAdapter: ActivityFeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customer_detail, R.id.root)

        customerId = intent.getStringExtra(EXTRA_CUSTOMER_ID).orEmpty()
        if (customerId.isEmpty()) {
            finish()
            return
        }

        activityAdapter = ActivityFeedAdapter { }
        findViewById<RecyclerView>(R.id.recyclerviewCustomerActivity).apply {
            layoutManager = LinearLayoutManager(this@CustomerDetailActivity)
            adapter = activityAdapter
        }

        presenter = CustomerDetailPresenter(this, CustomerDetailModel(renvestDb()), lifecycleScope)
        presenter.onViewReady(this, customerId)

        findViewById<MaterialToolbar>(R.id.toolbarCustomerDetail).setNavigationOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.buttonCustomerEdit).setOnClickListener {
            presenter.onEditClicked(this, customerId)
        }
        findViewById<MaterialButton>(R.id.buttonCustomerDelete).setOnClickListener {
            presenter.onDeleteClicked(this, customerId)
        }
    }

    override fun bindCustomer(name: String) {
        findViewById<TextView>(R.id.textviewCustomerDetailName).text = name
        findViewById<MaterialToolbar>(R.id.toolbarCustomerDetail).title = name
    }

    override fun bindActivityRows(items: List<ActivityEventRowUi>) {
        activityAdapter.submitList(items)
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showEditNameDialog(currentName: String, onSubmit: (String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_customer, null, false)
        val nameField = dialogView.findViewById<TextInputEditText>(R.id.edittextCustomerName)
        nameField.setText(currentName)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_edit_customer_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(nameField.text?.toString().orEmpty())
            }
            .show()
    }

    override fun showDeleteConfirm(name: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_customer_title)
            .setMessage(getString(R.string.dialog_delete_customer_message, name))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ -> onConfirm() }
            .show()
    }

    override fun closeScreen() {
        finish()
    }

    companion object {
        private const val EXTRA_CUSTOMER_ID = "customer_id"

        fun intent(context: Context, customerId: String): Intent =
            Intent(context, CustomerDetailActivity::class.java)
                .putExtra(EXTRA_CUSTOMER_ID, customerId)
    }
}
