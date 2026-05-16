package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomersPresenter(
    private val view: CustomersContract.View,
    private val model: CustomersModel,
    private val scope: CoroutineScope,
) : CustomersContract.Presenter {

    override fun onViewReady(context: Context) {
        bindScreen(context)
    }

    override fun onAddCustomerClicked(context: Context) {
        view.showAddCustomerDialog { raw -> onAddCustomerSubmitted(context, raw) }
    }

    override fun onAddCustomerSubmitted(context: Context, rawName: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.addCustomer(rawName) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    bindScreen(context)
                    view.showToast(context.getString(R.string.customer_added_confirmation))
                } else {
                    view.showToast(context.getString(R.string.error_field_required))
                }
            }
        }
    }

    override fun onCustomerLongPressed(context: Context, row: CustomerRowUi) {
        view.showDeleteCustomerConfirm(row.displayName) {
            scope.launch {
                withContext(Dispatchers.IO) { model.removeCustomer(row.id) }
                withContext(Dispatchers.Main) { bindScreen(context) }
            }
        }
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }

    private fun bindScreen(context: Context) {
        view.setHeaderBusinessName(model.businessDisplayName(context))
        view.setupNav(R.id.navCustomers)
        val counts = model.localDataCounts()
        val notRecorded = context.getString(R.string.metric_not_recorded)
        view.bindHeroMetrics(
            members = counts.customers.toString(),
            returnOrPlaceholder = notRecorded,
            atRisk = notRecorded,
        )
        val rows = model.loadCustomers()
        view.bindCustomerRows(rows)
        view.setCustomersEmptyVisible(rows.isEmpty())
    }
}
