package com.business.renvest.screens.activityfeed

import android.content.Context
import com.business.renvest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityFeedPresenter(
    private val view: ActivityFeedContract.View,
    private val model: ActivityFeedModel,
    private val scope: CoroutineScope,
) : ActivityFeedContract.Presenter {

    override fun onViewReady(context: Context) {
        bindScreen(context)
    }

    override fun onLogEventClicked(context: Context) {
        scope.launch {
            val customers = withContext(Dispatchers.IO) { model.loadCustomersForPicker() }
            withContext(Dispatchers.Main) {
                view.showAddActivityEventDialog(customers) { title, subtitle, customerId ->
                    onLogEventSubmitted(context, title, subtitle, customerId)
                }
            }
        }
    }

    override fun onLogEventSubmitted(
        context: Context,
        title: String,
        subtitle: String,
        customerId: String?,
    ) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.addEvent(title, subtitle, customerId) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    model.markOnboardingActivityStep(context)
                    bindScreen(context)
                    view.showToast(context.getString(R.string.activity_event_added_confirmation))
                } else {
                    view.showToast(context.getString(R.string.error_field_required))
                }
            }
        }
    }

    override fun onActivityLongPressed(context: Context, row: ActivityEventRowUi) {
        view.showDeleteActivityConfirm(row.title) {
            scope.launch {
                withContext(Dispatchers.IO) { model.removeEvent(row.id) }
                withContext(Dispatchers.Main) { bindScreen(context) }
            }
        }
    }

    override fun onStubInteraction() {
        view.showComingSoon()
    }

    private fun bindScreen(context: Context) {
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            val rows = withContext(Dispatchers.IO) { model.loadEvents() }
            withContext(Dispatchers.Main) {
                view.setHeaderBusinessName(model.businessDisplayName(context))
                view.setupNav(R.id.navActivity)
                view.bindHeroMetrics(
                    events = counts.activityEvents.toString(),
                    customers = counts.customers.toString(),
                    promotions = counts.promotions.toString(),
                )
                view.bindActivityRows(rows)
                view.setActivityEmptyVisible(rows.isEmpty())
            }
        }
    }
}
