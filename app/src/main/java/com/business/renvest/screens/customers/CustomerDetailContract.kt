package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.screens.activityfeed.ActivityEventRowUi

interface CustomerDetailContract {
    interface View {
        fun bindCustomer(name: String)
        fun bindActivityRows(items: List<ActivityEventRowUi>)
        fun showToast(message: String)
        fun showEditNameDialog(currentName: String, onSubmit: (String) -> Unit)
        fun showDeleteConfirm(name: String, onConfirm: () -> Unit)
        fun closeScreen()
    }

    interface Presenter {
        fun onViewReady(context: Context, customerId: String)
        fun onEditClicked(context: Context, customerId: String)
        fun onEditSubmitted(context: Context, customerId: String, rawName: String)
        fun onDeleteClicked(context: Context, customerId: String)
    }
}
