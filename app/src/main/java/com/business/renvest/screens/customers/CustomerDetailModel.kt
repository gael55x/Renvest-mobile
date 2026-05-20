package com.business.renvest.screens.customers

import com.business.renvest.data.local.ActivityEventType
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.ActivityEventEntity
import com.business.renvest.data.local.entity.CustomerEntity
import com.business.renvest.data.local.visitProgressForCustomer
import com.business.renvest.screens.activityfeed.ActivityEventRowUi
import java.util.UUID

class CustomerDetailModel(private val db: RenvestDatabase) {

    fun findCustomer(id: String): CustomerEntity? = db.customerDao().findById(id)

    fun loadActivityForCustomer(customerId: String): List<ActivityEventRowUi> =
        db.activityEventDao().listByCustomerId(customerId).map {
            ActivityEventRowUi(id = it.id, title = it.title, subtitle = it.subtitle, customerId = it.customerId)
        }

    fun visitProgress(customerId: String) = db.visitProgressForCustomer(customerId)

    fun logVisit(customerId: String): Boolean {
        val customer = findCustomer(customerId) ?: return false
        val now = System.currentTimeMillis()
        db.activityEventDao().insert(
            ActivityEventEntity(
                id = UUID.randomUUID().toString(),
                title = "Visit logged",
                subtitle = customer.displayName,
                customerId = customerId,
                eventType = ActivityEventType.VISIT,
                createdAt = now,
            ),
        )
        return true
    }

    fun redeemReward(customerId: String): Boolean {
        val customer = findCustomer(customerId) ?: return false
        val progress = visitProgress(customerId)
        if (!progress.readyForReward) return false
        val now = System.currentTimeMillis()
        db.activityEventDao().insert(
            ActivityEventEntity(
                id = UUID.randomUUID().toString(),
                title = "Reward redeemed",
                subtitle = customer.displayName,
                customerId = customerId,
                eventType = ActivityEventType.REWARD,
                createdAt = now,
            ),
        )
        return true
    }

    fun updateCustomerName(id: String, displayName: String): Boolean {
        val name = displayName.trim()
        if (name.isEmpty()) return false
        return db.customerDao().updateName(id, name, System.currentTimeMillis()) > 0
    }

    fun deleteCustomer(id: String) {
        db.activityEventDao().deleteByCustomerId(id)
        db.customerDao().deleteById(id)
    }
}
