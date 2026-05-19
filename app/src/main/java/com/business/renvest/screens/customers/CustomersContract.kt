package com.business.renvest.screens.customers

import android.content.Context
import androidx.annotation.IdRes
import com.business.renvest.R

interface CustomersContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int)
        fun bindHeroMetrics(members: String, returnOrPlaceholder: String, atRisk: String)
        fun bindCustomerRows(items: List<CustomerRowUi>)
        fun setCustomersEmptyVisible(visible: Boolean)
        fun showToast(message: String)
        fun showAddCustomerDialog(onSubmit: (String) -> Unit)
        fun showDeleteCustomerConfirm(displayName: String, onConfirm: () -> Unit)
        fun navigateToCustomerDetail(customerId: String)
        fun showComingSoon()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onAddCustomerClicked(context: Context)
        fun onAddCustomerSubmitted(context: Context, rawName: String)
        fun onCustomerClicked(context: Context, row: CustomerRowUi)
        fun onCustomerLongPressed(context: Context, row: CustomerRowUi)
        fun onStubInteraction()
    }
}
