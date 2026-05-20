package com.business.renvest.screens.customers

import android.content.Context
import androidx.annotation.IdRes

interface CustomersContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int, activityBadgeCount: Int)
        fun bindHeroMetrics(members: String, returnOrPlaceholder: String, atRisk: String)
        fun bindCustomerRows(items: List<CustomerRowUi>)
        fun setCustomersEmptyVisible(visible: Boolean)
        fun showToast(message: String)
        fun showAddCustomerDialog(onSubmit: (String) -> Unit)
        fun showDeleteCustomerConfirm(displayName: String, onConfirm: () -> Unit)
        fun showSortDialog(sortAscending: Boolean, onSelected: (Boolean) -> Unit)
        fun navigateToCustomerDetail(customerId: String)
        fun restoreSearchQuery(query: String)
        fun selectSegmentFilter(filter: CustomerSegmentFilter)
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onAddCustomerClicked(context: Context)
        fun onAddCustomerSubmitted(context: Context, rawName: String)
        fun onCustomerClicked(context: Context, row: CustomerRowUi)
        fun onCustomerLongPressed(context: Context, row: CustomerRowUi)
        fun onSearchQueryChanged(context: Context, query: String)
        fun onSegmentFilterSelected(context: Context, filter: CustomerSegmentFilter)
        fun onSortClicked(context: Context)
        fun onSortSelected(context: Context, sortAscending: Boolean)
    }
}
