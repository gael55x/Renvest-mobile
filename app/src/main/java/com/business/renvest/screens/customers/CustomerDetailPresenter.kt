package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerDetailPresenter(
    private val view: CustomerDetailContract.View,
    private val model: CustomerDetailModel,
    private val scope: CoroutineScope,
) : CustomerDetailContract.Presenter {

    override fun onViewReady(context: Context, customerId: String) {
        scope.launch {
            val customer = withContext(Dispatchers.IO) { model.findCustomer(customerId) }
            withContext(Dispatchers.Main) {
                if (customer == null) {
                    view.showToast(context.getString(R.string.customer_not_found))
                    view.closeScreen()
                    return@withContext
                }
                view.bindCustomer(customer.displayName)
                val events = withContext(Dispatchers.IO) { model.loadActivityForCustomer(customerId) }
                view.bindActivityRows(events)
            }
        }
    }

    override fun onEditClicked(context: Context, customerId: String) {
        scope.launch {
            val customer = withContext(Dispatchers.IO) { model.findCustomer(customerId) } ?: return@launch
            withContext(Dispatchers.Main) {
                view.showEditNameDialog(customer.displayName) { raw ->
                    onEditSubmitted(context, customerId, raw)
                }
            }
        }
    }

    override fun onEditSubmitted(context: Context, customerId: String, rawName: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.updateCustomerName(customerId, rawName) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    onViewReady(context, customerId)
                    view.showToast(context.getString(R.string.customer_updated_confirmation))
                } else {
                    view.showToast(context.getString(R.string.error_field_required))
                }
            }
        }
    }

    override fun onDeleteClicked(context: Context, customerId: String) {
        scope.launch {
            val customer = withContext(Dispatchers.IO) { model.findCustomer(customerId) } ?: return@launch
            withContext(Dispatchers.Main) {
                view.showDeleteConfirm(customer.displayName) {
                    scope.launch {
                        withContext(Dispatchers.IO) { model.deleteCustomer(customerId) }
                        withContext(Dispatchers.Main) { view.closeScreen() }
                    }
                }
            }
        }
    }
}
