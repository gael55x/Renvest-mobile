package com.business.renvest.screens.customers

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.showValidatedFormDialog
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class CustomersActivity : AppCompatActivity(), CustomersContract.View {

    private lateinit var presenter: CustomersPresenter
    private lateinit var customersAdapter: CustomersAdapter
    private lateinit var textviewCustomersEmpty: TextView
    private lateinit var searchField: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_customers, R.id.root)

        textviewCustomersEmpty = findViewById(R.id.textviewCustomersEmpty)
        searchField = findViewById(R.id.edittextSearchCustomersInput)
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
        presenter.restoreState(savedInstanceState)
        presenter.onViewReady(this)

        findViewById<View>(R.id.buttonFilter).setOnClickListener { presenter.onSortClicked(this) }
        findViewById<View>(R.id.textviewSort).setOnClickListener { presenter.onSortClicked(this) }
        searchField.doAfterTextChanged { text ->
            presenter.onSearchQueryChanged(this, text?.toString().orEmpty())
        }
        findViewById<View>(R.id.buttonAddCustomer).setOnClickListener {
            presenter.onAddCustomerClicked(this)
        }

        findViewById<ChipGroup>(R.id.chipgroupCustomerSegments).setOnCheckedStateChangeListener { group, checkedIds ->
            val id = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            val filter = when (group.findViewById<Chip>(id)?.id) {
                R.id.chipCustomerNearReward -> CustomerSegmentFilter.NEAR_REWARD
                R.id.chipCustomerReadyForReward -> CustomerSegmentFilter.READY_FOR_REWARD
                R.id.chipCustomerNew -> CustomerSegmentFilter.NEW
                else -> CustomerSegmentFilter.ALL
            }
            presenter.onSegmentFilterSelected(this, filter)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun restoreSearchQuery(query: String) {
        if (searchField.text?.toString() != query) {
            searchField.setText(query)
        }
    }

    override fun selectSegmentFilter(filter: CustomerSegmentFilter) {
        val chipId = when (filter) {
            CustomerSegmentFilter.NEAR_REWARD -> R.id.chipCustomerNearReward
            CustomerSegmentFilter.READY_FOR_REWARD -> R.id.chipCustomerReadyForReward
            CustomerSegmentFilter.NEW -> R.id.chipCustomerNew
            CustomerSegmentFilter.ALL -> R.id.chipCustomerAll
        }
        findViewById<Chip>(chipId).isChecked = true
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
        val nameLayout = dialogView.findViewById<com.google.android.material.textfield.TextInputLayout>(
            R.id.textinputCustomerName,
        )
        showValidatedFormDialog(
            titleRes = R.string.dialog_add_customer_title,
            dialogView = dialogView,
            nameLayout,
        ) {
            onSubmit(nameLayout.editText?.text?.toString().orEmpty())
        }
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
