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
            if (customer == null) {
                withContext(Dispatchers.Main) {
                    view.showToast(context.getString(R.string.customer_not_found))
                    view.closeScreen()
                }
                return@launch
            }
            val progress = withContext(Dispatchers.IO) { model.visitProgress(customerId) }
            val events = withContext(Dispatchers.IO) { model.loadActivityForCustomer(customerId) }
            withContext(Dispatchers.Main) {
                val progressLabel = context.getString(
                    R.string.customer_detail_progress_format,
                    progress.currentVisits,
                    progress.visitsRequired,
                )
                view.bindCustomer(customer.displayName, progressLabel)
                view.bindActivityRows(events)
                view.setActivityEmptyVisible(events.isEmpty())
            }
        }
    }

    override fun onLogVisitClicked(context: Context, customerId: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.logVisit(customerId) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    view.showToast(context.getString(R.string.activity_event_added_confirmation))
                    onViewReady(context, customerId)
                } else {
                    view.showToast(context.getString(R.string.error_field_required))
                }
            }
        }
    }

    override fun onRedeemRewardClicked(context: Context, customerId: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.redeemReward(customerId) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    view.showToast(context.getString(R.string.customer_reward_redeemed))
                    onViewReady(context, customerId)
                } else {
                    view.showToast(context.getString(R.string.customer_reward_not_ready))
                }
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
