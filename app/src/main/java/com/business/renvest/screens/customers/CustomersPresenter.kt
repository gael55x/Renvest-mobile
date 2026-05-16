package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.R

class CustomersPresenter(
    private val view: CustomersContract.View,
    private val model: CustomersModel,
) : CustomersContract.Presenter {

    override fun onViewReady(context: Context) {
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

    override fun onStubInteraction() {
        view.showComingSoon()
    }
}
