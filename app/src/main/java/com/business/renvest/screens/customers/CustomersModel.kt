package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.CustomerEntity
import com.business.renvest.data.local.logActivity
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore
import java.util.UUID

class CustomersModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadCustomers(searchQuery: String, sortAscending: Boolean): List<CustomerRowUi> {
        val query = searchQuery.trim().lowercase()
        return db.customerDao().listAll()
            .map { CustomerRowUi(id = it.id, displayName = it.displayName) }
            .filter { query.isEmpty() || it.displayName.lowercase().contains(query) }
            .let { list -> if (sortAscending) list.sortedBy { it.displayName.lowercase() } else list.sortedByDescending { it.displayName.lowercase() } }
    }

    /** @return false if name is blank after trim */
    fun addCustomer(displayName: String): Boolean {
        val name = displayName.trim()
        if (name.isEmpty()) return false
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        db.customerDao().insert(
            CustomerEntity(
                id = id,
                displayName = name,
                createdAt = now,
                updatedAt = now,
            ),
        )
        db.logActivity(title = "Customer added", subtitle = name, customerId = id)
        return true
    }

    fun removeCustomer(id: String) {
        db.customerDao().deleteById(id)
    }

    fun markOnboardingCustomerStep(context: Context) {
        authStore.markOnboardingStep(context, AuthStore.STEP_CUSTOMER)
    }
}
