package com.business.renvest.screens.customers

import android.content.Context
import com.business.renvest.data.local.LocalDataCounts
import com.business.renvest.data.local.RenvestDatabase
import com.business.renvest.data.local.entity.CustomerEntity
import com.business.renvest.data.local.ActivityEventType
import com.business.renvest.data.local.logActivity
import com.business.renvest.data.local.visitProgressForCustomer
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.data.repository.AuthStore
import java.util.UUID

class CustomersModel(
    private val authStore: AuthStore,
    private val db: RenvestDatabase,
) {

    fun businessDisplayName(context: Context): String = authStore.businessDisplayName(context)

    fun localDataCounts(): LocalDataCounts = db.localDataCounts()

    fun loadCustomers(
        searchQuery: String,
        sortAscending: Boolean,
        segment: CustomerSegmentFilter,
    ): List<CustomerRowUi> {
        val query = searchQuery.trim().lowercase()
        return db.customerDao().listAll()
            .filter { entity ->
                matchesSegment(db.visitProgressForCustomer(entity.id), segment) &&
                    (query.isEmpty() || entity.displayName.lowercase().contains(query))
            }
            .map { entity ->
                val progress = db.visitProgressForCustomer(entity.id)
                CustomerRowUi(
                    id = entity.id,
                    displayName = entity.displayName,
                    progressSummary = "${progress.currentVisits}/${progress.visitsRequired} visits",
                    lastVisitSummary = progress.lastVisitLabel,
                )
            }
            .let { list ->
                if (sortAscending) list.sortedBy { it.displayName.lowercase() }
                else list.sortedByDescending { it.displayName.lowercase() }
            }
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
        db.logActivity(
            title = "Customer added",
            subtitle = name,
            customerId = id,
            eventType = ActivityEventType.SYSTEM,
        )
        return true
    }

    fun removeCustomer(id: String) {
        db.activityEventDao().deleteByCustomerId(id)
        db.customerDao().deleteById(id)
    }

    fun markOnboardingCustomerStep(context: Context) {
        authStore.markOnboardingStep(context, AuthStore.STEP_CUSTOMER)
    }

    private fun matchesSegment(
        progress: com.business.renvest.data.local.CustomerVisitProgress,
        segment: CustomerSegmentFilter,
    ): Boolean = when (segment) {
        CustomerSegmentFilter.ALL -> true
        CustomerSegmentFilter.NEAR_REWARD -> progress.nearReward
        CustomerSegmentFilter.READY_FOR_REWARD -> progress.readyForReward
        CustomerSegmentFilter.NEW -> progress.currentVisits == 0 && progress.lastVisitLabel.isEmpty()
    }
}
