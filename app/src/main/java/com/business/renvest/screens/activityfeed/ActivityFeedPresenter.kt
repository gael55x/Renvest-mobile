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
        view.showAddActivityEventDialog { title, subtitle ->
            onAddActivitySubmitted(context, title, subtitle)
        }
    }

    override fun onAddActivitySubmitted(context: Context, title: String, subtitle: String) {
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.addEvent(title, subtitle) }
            withContext(Dispatchers.Main) {
                if (ok) {
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
        view.setHeaderBusinessName(model.businessDisplayName(context))
        view.setupNav(R.id.navActivity)
        val counts = model.localDataCounts()
        view.bindHeroMetrics(
            events = counts.activityEvents.toString(),
            customers = counts.customers.toString(),
            promotions = counts.promotions.toString(),
        )
        val rows = model.loadEvents()
        view.bindActivityRows(rows)
        view.setActivityEmptyVisible(rows.isEmpty())
    }
}
