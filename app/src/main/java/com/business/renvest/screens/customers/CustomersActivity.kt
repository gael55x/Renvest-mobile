package com.business.renvest.screens.customers

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setClickListeners
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.business.renvest.utils.startActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class CustomersActivity : AppCompatActivity(), CustomersContract.View {

    private lateinit var presenter: CustomersPresenter
    private lateinit var customersAdapter: CustomersAdapter
    private lateinit var textviewCustomersEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)

        textviewCustomersEmpty = findViewById(R.id.textviewCustomersEmpty)
        customersAdapter = CustomersAdapter(
            onClick = { presenter.onCustomerClicked(this, it) },
            onLongClick = { presenter.onCustomerLongPressed(this, it) },
        )
        findViewById<RecyclerView>(R.id.recyclerviewCustomers).apply {
            layoutManager = LinearLayoutManager(this@CustomersActivity)
            adapter = customersAdapter
        }

        presenter = CustomersPresenter(
            this,
            CustomersModel(authStore(), renvestDb()),
            lifecycleScope,
        )
        presenter.onViewReady(this)

        findViewById<View>(R.id.buttonFilter).setOnClickListener { presenter.onSortClicked(this) }
        findViewById<View>(R.id.textviewSort).setOnClickListener { presenter.onSortClicked(this) }
        findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.edittextSearchCustomersInput)
            .doAfterTextChanged { text ->
                presenter.onSearchQueryChanged(this, text?.toString().orEmpty())
            }
        findViewById<View>(R.id.buttonAddCustomer).setOnClickListener {
            presenter.onAddCustomerClicked(this)
        }
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int, activityBadgeCount: Int) {
        setupMainBottomNavigation(selectedItemId, activityBadgeCount)
    }

    override fun bindHeroMetrics(members: String, returnOrPlaceholder: String, atRisk: String) {
        findViewById<TextView>(R.id.textviewCustomersHeroMembers).text = members
        findViewById<TextView>(R.id.textviewCustomersHeroReturn).text = returnOrPlaceholder
        findViewById<TextView>(R.id.textviewCustomersHeroAtRisk).text = atRisk
    }

    override fun bindCustomerRows(items: List<CustomerRowUi>) {
        customersAdapter.submitList(items)
    }

    override fun setCustomersEmptyVisible(visible: Boolean) {
        textviewCustomersEmpty.isVisible = visible
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showAddCustomerDialog(onSubmit: (String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_customer, null, false)
        val nameField = dialogView.findViewById<TextInputEditText>(R.id.edittextCustomerName)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_add_customer_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(nameField.text?.toString().orEmpty())
            }
            .show()
    }

    override fun showDeleteCustomerConfirm(displayName: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_customer_title)
            .setMessage(getString(R.string.dialog_delete_customer_message, displayName))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ -> onConfirm() }
            .show()
    }

    override fun navigateToCustomerDetail(customerId: String) {
        startActivity(CustomerDetailActivity.intent(this, customerId))
    }

    override fun showSortDialog(sortAscending: Boolean, onSelected: (Boolean) -> Unit) {
        val options = arrayOf(
            getString(R.string.customers_sort_name_asc),
            getString(R.string.customers_sort_name_desc),
        )
        val checked = if (sortAscending) 0 else 1
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.customers_sort_title)
            .setSingleChoiceItems(options, checked) { dialog, which ->
                onSelected(which == 0)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
