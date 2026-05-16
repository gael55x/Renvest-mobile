package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.CustomerEntity
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore
import java.util.UUID

class CustomersModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadCustomers(): List<CustomerRowUi> =
        db.customerDao().listAll().map { CustomerRowUi(id = it.id, displayName = it.displayName) }

    /** @return false if name is blank after trim */
    fun addCustomer(displayName: String): Boolean {
        val name = displayName.trim()
        if (name.isEmpty()) return false
        val now = System.currentTimeMillis()
        db.customerDao().insert(
            CustomerEntity(
                id = UUID.randomUUID().toString(),
                displayName = name,
                createdAt = now,
                updatedAt = now,
            ),
        )
        return true
    }

    fun removeCustomer(id: String) {
        db.customerDao().deleteById(id)
    }
}
