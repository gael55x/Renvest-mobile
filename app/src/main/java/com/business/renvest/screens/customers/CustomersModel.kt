package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore

class CustomersModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadCustomers(): List<CustomerRowUi> =
        db.customerDao().listAll().map { CustomerRowUi(id = it.id, displayName = it.displayName) }
}
