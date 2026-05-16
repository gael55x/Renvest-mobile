package com.business.renvest.screens.customers

import android.content.Context
import androidx.annotation.IdRes

interface CustomersContract {
    interface View {
        fun setHeaderBusinessName(text: String)
        fun setupNav(@IdRes selectedItemId: Int)
        fun bindHeroMetrics(members: String, returnOrPlaceholder: String, atRisk: String)
        fun bindCustomerRows(items: List<CustomerRowUi>)
        fun setCustomersEmptyVisible(visible: Boolean)
        fun showComingSoon()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onStubInteraction()
    }
}
