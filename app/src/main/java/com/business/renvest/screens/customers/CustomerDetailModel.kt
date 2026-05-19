package com.business.renvest.screens.customers

import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.CustomerEntity
import com.business.renvest.screens.activityfeed.ActivityEventRowUi

class CustomerDetailModel(private val db: RenvestDatabase) {

    fun findCustomer(id: String): CustomerEntity? = db.customerDao().findById(id)

    fun loadActivityForCustomer(customerId: String): List<ActivityEventRowUi> =
        db.activityEventDao().listByCustomerId(customerId).map {
            ActivityEventRowUi(id = it.id, title = it.title, subtitle = it.subtitle, customerId = it.customerId)
        }

    fun updateCustomerName(id: String, displayName: String): Boolean {
        val name = displayName.trim()
        if (name.isEmpty()) return false
        return db.customerDao().updateName(id, name, System.currentTimeMillis()) > 0
    }

    fun deleteCustomer(id: String) {
        db.customerDao().deleteById(id)
    }
}
