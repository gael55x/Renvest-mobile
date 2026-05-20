package com.business.renvest.screens.customers

import android.content.Context
import android.os.Bundle
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

    private var searchQuery: String = ""
    private var sortAscending: Boolean = true
    private var segmentFilter: CustomerSegmentFilter = CustomerSegmentFilter.ALL

    override fun onViewReady(context: Context) {
        bindScreen(context)
    }

    fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return
        searchQuery = savedInstanceState.getString(KEY_SEARCH).orEmpty()
        sortAscending = savedInstanceState.getBoolean(KEY_SORT, true)
        segmentFilter = CustomerSegmentFilter.entries[
            savedInstanceState.getInt(KEY_SEGMENT, CustomerSegmentFilter.ALL.ordinal)
        ]
        view.restoreSearchQuery(searchQuery)
        view.selectSegmentFilter(segmentFilter)
    }

    fun saveState(outState: Bundle) {
        outState.putString(KEY_SEARCH, searchQuery)
        outState.putBoolean(KEY_SORT, sortAscending)
        outState.putInt(KEY_SEGMENT, segmentFilter.ordinal)
    }

    override fun onAddCustomerClicked(context: Context) {
        view.showAddCustomerDialog { raw -> onAddCustomerSubmitted(context, raw) }
    }

    override fun onAddCustomerSubmitted(context: Context, rawName: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.addCustomer(rawName) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    model.markOnboardingCustomerStep(context)
                    bindScreen(context)
                    view.showToast(context.getString(R.string.customer_added_confirmation))
                } else {
                    view.showToast(context.getString(R.string.error_field_required))
                }
            }
        }
    }

    override fun onCustomerClicked(context: Context, row: CustomerRowUi) {
        view.navigateToCustomerDetail(row.id)
    }

    override fun onCustomerLongPressed(context: Context, row: CustomerRowUi) {
        view.showDeleteCustomerConfirm(row.displayName) {
            scope.launch {
                withContext(Dispatchers.IO) { model.removeCustomer(row.id) }
                withContext(Dispatchers.Main) { bindScreen(context) }
            }
        }
    }

    override fun onSearchQueryChanged(context: Context, query: String) {
        searchQuery = query
        bindScreen(context)
    }

    override fun onSegmentFilterSelected(context: Context, filter: CustomerSegmentFilter) {
        segmentFilter = filter
        bindScreen(context)
    }

    override fun onSortClicked(context: Context) {
        view.showSortDialog(sortAscending) { onSortSelected(context, it) }
    }

    override fun onSortSelected(context: Context, sortAscending: Boolean) {
        this.sortAscending = sortAscending
        bindScreen(context)
    }

    private fun bindScreen(context: Context) {
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            val rows = withContext(Dispatchers.IO) {
                model.loadCustomers(searchQuery, sortAscending, segmentFilter)
            }
            val notRecorded = context.getString(R.string.metric_not_recorded)
            withContext(Dispatchers.Main) {
                view.setHeaderBusinessName(model.businessDisplayName(context))
                view.setupNav(R.id.navCustomers, counts.activityEvents)
                view.bindHeroMetrics(
                    members = counts.customers.toString(),
                    returnOrPlaceholder = notRecorded,
                    atRisk = notRecorded,
                )
                view.bindCustomerRows(rows)
                view.setCustomersEmptyVisible(rows.isEmpty())
            }
        }
    }

    companion object {
        private const val KEY_SEARCH = "customers_search"
        private const val KEY_SORT = "customers_sort_asc"
        private const val KEY_SEGMENT = "customers_segment"
    }
}
